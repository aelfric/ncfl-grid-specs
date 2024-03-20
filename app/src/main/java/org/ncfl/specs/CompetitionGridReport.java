package org.ncfl.specs;

import j2html.tags.specialized.TrTag;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static j2html.TagCreator.td;
import static j2html.TagCreator.tr;
import static j2html.TagCreator.table;

public class CompetitionGridReport implements Reporter {

    private record GridRow(
        String Prelims,
        String PlayIn,
        String Doubles,
        String Octos,
        String Quarters,
        String Semis,
        String Finals
    ){

    }

    @Override
    public String process(List<Hotel> hotelList) {
        return table()
            .with(
                hotelList
                    .stream()
                    .map(this::aggregate)
                    .flatMap(this::something)
            )
            .render();
    }

    private Stream<TrTag> something(Map<RoomID, List<RoomUsage>> roomIDListMap) {

        return roomIDListMap
            .entrySet()
            .stream()
            .sorted(
                Comparator.comparing(
                    (Map.Entry<RoomID, List<RoomUsage>> roomIDListEntry) ->
                        roomIDListEntry.getKey().name(),
                    AlphaNumComparator.ALPHANUM
                )
            )
            .map(entry -> tr(
                td(entry.getKey().venue()),
                td(entry.getKey().name()),
                td(
                    getActivities(entry.getValue())
                )));
    }

    private static String getActivities(List<RoomUsage> value) {
        return value
            .stream()
            .map(RoomUsage::activity)
            .distinct()
            .map(activity -> activity.replaceAll("#\\d+", "Prelim"))
            .collect(Collectors.joining(","));
    }

    private Map<RoomID, List<RoomUsage>> aggregate(Hotel hotel) {
        return hotel
            .roomUsage()
            .stream()
            .collect(Collectors.groupingBy(RoomUsage::key));
    }
}
