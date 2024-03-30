package org.ncfl.specs;

import j2html.TagCreator;
import j2html.tags.DomContent;
import j2html.tags.Text;
import j2html.tags.specialized.BodyTag;
import jakarta.annotation.Nonnull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static j2html.TagCreator.*;


public class RoomSpecReport implements Reporter {
    protected static final Logger logger = LogManager.getLogger();
    private static final DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd");

    public String process(List<Hotel> hotelRoomUsage) {
        BodyTag body = body();
        for (Hotel hotel : hotelRoomUsage) {
            body = body
                .with(h1(hotel.name()))
                .with(
                    List.of(
                        filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 21)),
                        filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 22)),
                        filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 23)),
                        filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 24)),
                        filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 25)),
                        filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 26)),
                        filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 27)),
                        filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 28))
                    )
                );
        }
        return body.render();
    }


    private DomContent filterAndPrintRooms(List<RoomUsage> data,
                                           @Nonnull LocalDate localDate) {
        Map<RoomID, List<RoomUsage>> roomMap = data
            .stream()
            .filter(u -> localDate.equals(u.date()))
            .collect(Collectors.groupingBy(RoomUsage::key));

        if (roomMap.isEmpty()) {
            return section();
        }
        return section(h2("%s Room Sets".formatted(localDate.format(dayFormatter))))
            .with(printRoomUsages(dedupeCubicles(roomMap)));
    }

    private Map<RoomID, List<RoomUsage>> dedupeCubicles(Map<RoomID, List<RoomUsage>> roomMap) {
        final HashMap<RoomID, List<RoomUsage>> map = new HashMap<>(roomMap.size());
        for (Map.Entry<RoomID, List<RoomUsage>> entry : roomMap.entrySet()) {
            final RoomID roomID = entry.getKey();
            if (roomID.name().contains("-")) {
                RoomID newKey = new RoomID(
                    roomID.venue(),
                    roomID.floor(),
                    roomID.name().substring(0, roomID.name().indexOf("-"))
                );
                map.compute(newKey, (roomID1, roomUsages) -> mergeUsages(entry, roomUsages));
            } else if (roomID.name().contains("Cubicle")) {
                RoomID newKey = new RoomID(
                    roomID.venue(),
                    roomID.floor(),
                    roomID.name().substring(0, roomID.name().indexOf("Cubicle"))
                );
                map.compute(newKey, (roomID1, roomUsages) -> mergeUsages(entry, roomUsages));
            } else {
                map.put(roomID, entry.getValue());
            }
        }
        return map;
    }

    private static List<RoomUsage> mergeUsages(Map.Entry<RoomID, List<RoomUsage>> entry, List<RoomUsage> roomUsages) {
        if (roomUsages == null) {
            return entry.getValue();
        }
        final ArrayList<RoomUsage> newRoomUsages = new ArrayList<>();
        newRoomUsages.addAll(roomUsages);
        newRoomUsages.addAll(entry.getValue());
        return newRoomUsages;
    }


    private Stream<DomContent> printRoomUsages(Map<RoomID, List<RoomUsage>> roomMap) {
        return roomMap
            .entrySet()
            .stream()
            .sorted(
                Comparator.comparing(
                    (Map.Entry<RoomID, List<RoomUsage>> roomIDListEntry) ->
                        roomIDListEntry.getKey().name(),
                    AlphaNumComparator.ALPHANUM
                )
            )
            .map(this::roomUsageDiv);
    }

    private DomContent roomUsageDiv(Map.Entry<RoomID, List<RoomUsage>> entry) {
        return div(h3(entry.getKey().name()))
            .with(
                formatRoomUsages(entry.getValue())
            );
    }

    private static Stream<DomContent> formatRoomUsages(List<RoomUsage> roomUsages) {
        return
            roomUsages.stream()
                .collect(Collectors.groupingBy(WhenAndWhat::fromRoomUsage))
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(e -> e.getKey().start()))
                .map(
                    e -> {
                        final WhenAndWhat key = e.getKey();
                        final List<RoomUsage> usages = e.getValue();
                        final RoomSet roomSet = Objects.requireNonNullElse(
                            key.roomSet(),
                            RoomSet.SPECIAL_OTHER
                        );
                        final Set<String> notes = usages.stream()
                            .map(RoomUsage::hotelNotes)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());

                        final DomContent notesHtml = notes.isEmpty() ? text("") : ul().with(notes.stream()
                            .map(TagCreator::li));

                        final Text timeRange = text("%s - %s ".formatted(key.start(), key.end()));
                        if (usages.size() == 1) {
                            return
                                div(
                                    h4(
                                        timeRange,
                                        a(String.valueOf(roomSet))
                                            .attr("href", roomSet.href()),
                                        text(" (%s)".formatted(usages.get(0).activity()))
                                    ),
                                    ul(
                                        li(
                                            strong("Room Setup:"),
                                            roomSet.description()
                                        )
                                    ),
                                    notesHtml
                                );
                        } else {
                            return
                                div(
                                    h4(
                                        timeRange,
                                        a(String.valueOf(roomSet))
                                            .attr("href", roomSet.href())
                                    ),
                                    ul(
                                        li(
                                            strong("Room Setup:"),
                                            roomSet.description()
                                        ),
                                        li(strong("Activities"),
                                            ul()
                                                .with(
                                                    usages.stream()
                                                        .map(RoomUsage::activity)
                                                        .sorted(AlphaNumComparator.ALPHANUM)
                                                        .map(TagCreator::li)
                                                )
                                        )
                                    ),
                                    notesHtml
                                );
                        }
                    }
                );
    }

}
