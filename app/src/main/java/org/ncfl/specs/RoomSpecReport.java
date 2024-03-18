package org.ncfl.specs;

import j2html.tags.DomContent;
import j2html.tags.specialized.BodyTag;
import j2html.tags.specialized.LiTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static j2html.TagCreator.*;


public class RoomSpecReport implements Reporter {
    protected static final Logger logger = LogManager.getLogger();

    public String process(List<Hotel> hotelRoomUsage) {
        BodyTag body = body();
        for (Hotel hotel : hotelRoomUsage) {
            body = body
                .with(h1(hotel.name()))
                .with(
                    List.of(
                        filterAndPrintRooms("Saturday Room Sets", hotel.roomUsage(), DayOfWeek.SATURDAY),
                        filterAndPrintRooms("Sunday Room Sets", hotel.roomUsage(), DayOfWeek.SUNDAY)
                    )
                );
        }
        return body.render();
    }


    private DomContent filterAndPrintRooms(String title,
                                           List<RoomUsage> data,
                                           DayOfWeek dayOfWeek) {
        Map<RoomID, List<RoomUsage>> roomMap = data
            .stream()
            .filter(u -> u.day() == dayOfWeek)
            .collect(Collectors.groupingBy(RoomUsage::key));

        return section(h2(title))
            .with(printRoomUsages(dedupeCubicles(roomMap)));
    }

    private Map<RoomID, List<RoomUsage>> dedupeCubicles(Map<RoomID, List<RoomUsage>> roomMap) {
        final HashMap<RoomID, List<RoomUsage>> map = new HashMap<>(roomMap.size());
        for (Map.Entry<RoomID, List<RoomUsage>> entry : roomMap.entrySet()) {
            final RoomID roomID = entry.getKey();
            if(roomID.name().contains("-")) {
                RoomID newKey = new RoomID(roomID.venue(), roomID.floor(), roomID.name()
                    .substring(0, roomID.name().indexOf("-")));
                map.compute(newKey, (roomID1, roomUsages) -> {
                    if(roomUsages == null){
                        return entry.getValue();
                    }
                    final ArrayList<RoomUsage> newRoomUsages = new ArrayList<>();
                    newRoomUsages.addAll(roomUsages);
                    newRoomUsages.addAll(entry.getValue());
                    return newRoomUsages;
                });
            } else if (roomID.name().contains("Cubicle")){
                RoomID newKey = new RoomID(roomID.venue(), roomID.floor(), roomID.name()
                    .substring(0, roomID.name().indexOf("Cubicle")));
                map.compute(newKey, (roomID1, roomUsages) -> {
                    if(roomUsages == null){
                        return entry.getValue();
                    }
                    final ArrayList<RoomUsage> newRoomUsages = new ArrayList<>();
                    newRoomUsages.addAll(roomUsages);
                    newRoomUsages.addAll(entry.getValue());
                    return newRoomUsages;
                });
            } else {
                map.put(roomID, entry.getValue());
            }
        }
        return map;
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
                ul()
                    .with(
                        entry
                            .getValue()
                            .stream()
                            .map(roomUsage -> {
                                final LiTag li = li(
                                    span("[%s - %s] ".formatted(roomUsage.start(),
                                        roomUsage.end())),
                                    a(String.valueOf(roomUsage.roomSet()))
                                        .attr("href",
                                            Objects.requireNonNullElse(roomUsage.roomSet(),
                                                RoomSet.SPECIAL_OTHER).href()),
                                    text(" ("),
                                    text(roomUsage.activity()),
                                    text(")")
                                );
                                if (roomUsage.hotelNotes() != null) {
                                    return li.with(
                                        br(),
                                        i(roomUsage.hotelNotes())
                                    );
                                } else {
                                    return li;
                                }
                            })
                    )
            );
    }

}
