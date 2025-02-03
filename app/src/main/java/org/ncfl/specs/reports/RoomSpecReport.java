package org.ncfl.specs.reports;

import j2html.TagCreator;
import j2html.tags.DomContent;
import j2html.tags.Text;
import j2html.tags.specialized.BodyTag;
import j2html.tags.specialized.DivTag;
import j2html.tags.specialized.LiTag;
import jakarta.annotation.Nonnull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ncfl.specs.model.WhenAndWhat;
import org.ncfl.specs.model.Hotel;
import org.ncfl.specs.model.RoomID;
import org.ncfl.specs.model.RoomSet;
import org.ncfl.specs.model.RoomUsage;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static j2html.TagCreator.*;


public class RoomSpecReport implements Reporter {
    private static final DateTimeFormatter
        dayFormatter =
        DateTimeFormatter.ofPattern("EEE, MMM dd");

    public String process(List<Hotel> hotelRoomUsage) {
        BodyTag body = body();
        for (Hotel hotel : hotelRoomUsage) {
            body = body
                .with(h1(hotel.name()))
                .with(
                    getAVNeeds(hotel)
                )
                .with(
                    getReaderBoards(hotel)
                )
                .with(
                    getCateringNeeds(hotel)
                )
                .with(
                    h2("All-Gender Restrooms"),
                    p("Please designate at least one public restroom as an all-gender " +
                        "restroom and let me know where it is so I can post it on our " +
                        "communications document.")
                )
                .with(
                    getRoomSets(hotel)
                );
        }
        return body.render();
    }

    private List<DomContent> getReaderBoards(Hotel hotel) {
        Stream<DomContent> stream = hotel
            .roomUsage()
            .stream()
            .filter(RoomUsage::publish)
            .collect(Collectors.groupingBy(RoomUsage::date))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .map(
                u -> li()
                    .with(text(u.getKey().format(dayFormatter)))
                    .with(ul().with(
                        u.getValue().stream()
                            .sorted(Comparator.comparing(RoomUsage::name,
                                AlphaNumComparator.ALPHANUM))
                            .map(this::readerBoard)
                    ))
            );


        return List.of(
            h2("Readerboard"),
            ul().with(stream));
    }

    private List<DomContent> getAVNeeds(Hotel hotel) {
        Stream<DomContent> stream = hotel
            .roomUsage()
            .stream()
            .filter(roomUsage -> roomUsage.avNeeds() != null && !roomUsage.avNeeds().isBlank())
            .collect(Collectors.groupingBy(RoomUsage::date))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .map(
                u -> li()
                    .with(text(u.getKey().format(dayFormatter)))
                    .with(
                        ul()
                            .with(
                                u.getValue().stream()
                                    .sorted(Comparator.comparing(RoomUsage::name,
                                        AlphaNumComparator.ALPHANUM))
                                    .map(roomUsage -> li(
                                        strong(roomUsage.name()),
                                        text(": "),
                                        text(roomUsage.avNeeds())
                                    ))
                            )
                    )
            );


        return List.of(
            h2("A/V Needs"),
            ul().with(stream));
    }

    private List<DomContent> getCateringNeeds(Hotel hotel) {
        Stream<DomContent> stream = hotel
            .roomUsage()
            .stream()
            .filter(roomUsage -> roomUsage.catering() != null && !roomUsage.catering()
                .isEmpty())
            .collect(Collectors.groupingBy(RoomUsage::date))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .map(
                u -> li()
                    .with(text(u.getKey().format(dayFormatter)))
                    .with(ul().with(
                        u.getValue().stream()
                            .sorted(Comparator.comparing(RoomUsage::name,
                                AlphaNumComparator.ALPHANUM))
                            .map(roomUsage -> li(
                                strong(roomUsage.name()),
                                text(": [TO FILL IN]")
                            ))
                    ))
            );


        return List.of(
            h2("Catering Needs"),
            ul().with(stream));
    }

    private DomContent readerBoard(RoomUsage roomUsage) {
        return li(
            strong(roomUsage.name()),
            text(": "),
            text(roomUsage.activity())
        );
    }

    private List<DomContent> getRoomSets(Hotel hotel) {
        return List.of(
            filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 21)),
            filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 22)),
            filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 23)),
            filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 24)),
            filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 25)),
            filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 26)),
            filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 27)),
            filterAndPrintRooms(hotel.roomUsage(), LocalDate.of(2024, Month.MAY, 28))
        );
    }


    private DomContent filterAndPrintRooms(List<RoomUsage> data,
                                           @Nonnull LocalDate localDate) {
        Map<RoomID, List<RoomUsage>> roomMap = data
            .stream()
            .filter(u -> localDate.equals(u.date()))
            .filter(u -> Objects.nonNull(u.activity()) && !u.activity().isEmpty())
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

    private static List<RoomUsage> mergeUsages(Map.Entry<RoomID, List<RoomUsage>> entry,
                                               List<RoomUsage> roomUsages) {
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
                .map(RoomSpecReport::formatRoomUsage);
    }

    private static DivTag formatRoomUsage(Map.Entry<WhenAndWhat, List<RoomUsage>> e) {
        final WhenAndWhat key = e.getKey();
        final List<RoomUsage> usages = e.getValue();
        final RoomSet roomSet = Objects.requireNonNullElse(
            key.roomSet(),
            RoomSet.SPECIAL_OTHER
        );
        final List<LiTag> notes = usages.stream()
            .map(RoomUsage::hotelNotes)
            .filter(n -> n != null && !n.isEmpty())
            .distinct()
            .map(TagCreator::li)
            .toList();

        final Text
            timeRange =
            text("%s - %s ".formatted(key.start(), key.end()));
        if (usages.size() == 1) {
            return div(
                p(
                    timeRange,
                    a(String.valueOf(roomSet))
                        .attr("href", roomSet.href()),
                    text(" (%s)".formatted(usages.get(0).activity()))
                ),
                ul(
                    li(
                        strong("Room Setup:"),
                        roomSet.description().with(notes)
                    )
                )
                    .with(
                        usages.get(0).publish() ? li(
                            strong("Readerboard: "),
                            text(usages.get(0).activity())
                        ) : text(""),
                        (usages.get(0).avNeeds() != null && !usages.get(0).avNeeds().isBlank()) ? li(
                            strong("A/V Needs: "),
                            text(usages.get(0).avNeeds())
                        ) : text(""),
                        usages.get(0).catering() != null && !usages.get(0)
                            .catering().isEmpty() ? li(
                            strong("Catering Needs: "),
                            text("[TO FILL IN]")
                        ) : text("")
                    )
            );
        } else {
            return div(
                p(
                    timeRange,
                    a(String.valueOf(roomSet))
                        .attr("href", roomSet.href())
                ),
                ul(
                    li(
                        strong("Room Setup:"),
                        roomSet.description().with(notes)
                    ),
                    li(strong("Activities"),
                        ul()
                            .with(
                                usages.stream()
                                    .map(RoomUsage::activity)
                                    .sorted(AlphaNumComparator.ALPHANUM)
                                    .distinct()
                                    .map(TagCreator::li)
                            )
                    )
                )
            );
        }
    }

}
