package org.ncfl.specs.reports;

import j2html.tags.DomContent;
import j2html.tags.specialized.TrTag;
import org.ncfl.specs.model.Hotel;
import org.ncfl.specs.model.RoomID;
import org.ncfl.specs.model.RoomUsage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static j2html.TagCreator.*;

public class CompetitionGridReport implements Reporter {

    @Override
    public String process(List<Hotel> hotelList) {
        return table().attr("border","1")
            .with(
                thead(
                    tr(
                        th("Venue"),
                        th("Room"),
                        th("Activities"),
                        th("Prelims"),
                        th("Play-In"),
                        th("Doubles"),
                        th("Octos"),
                        th("Quarters"),
                        th("Semis"),
                        th("Finals"),
                        th("???")
                    )
                )
            )
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
            .map(entry -> {
                final String activities = getActivities(entry.getValue());
                    return tr(
                        td(entry.getKey().venue()),
                        td(entry.getKey().name()),
                        td(
                            activities
                        ))
                        .with(columns(activities));
                }
            );
    }

    private List<DomContent> columns(String value) {

        List<String> prelims = new LinkedList<>();
        List<String> playIn = new LinkedList<>();
        List<String> doubles = new LinkedList<>();
        List<String> octos = new LinkedList<>();
        List<String> quarters = new LinkedList<>();
        List<String> semis = new LinkedList<>();
        List<String> finals = new LinkedList<>();
        List<String> unknown = new LinkedList<>();

        for (String activity : value.split(",")) {
            if(activity.contains("Prelim")) {
                prelims.add(activity);
            } else if(activity.contains("Speech Competition Room #")){
                prelims.add(activity);
            } else if(activity.contains("Play-In")){
                playIn.add(activity);
            } else if(activity.contains("Double-OctoFinals") || activity.contains("Doubles")){
                doubles.add(activity);
            } else if(activity.contains("Octos")){
                octos.add(activity);
            } else if(activity.contains("Quarters")){
                quarters.add(activity);
            } else if(activity.contains("Semis")){
                semis.add(activity);
            } else if(activity.contains("Finals")){
                finals.add(activity);
            } else {
                unknown.add(activity);
            }

        }
        return List.of(
            td(String.join(", ", prelims)),
            td(String.join(", ", playIn)),
            td(String.join(", ", doubles)),
            td(String.join(", ", octos)),
            td(String.join(", ", quarters)),
            td(String.join(", ", semis)),
            td(String.join(", ", finals)),
            td(String.join(", ", unknown))
        );
    }

    private static String getActivities(List<RoomUsage> value) {
        return value
            .stream()
            .filter(usage -> Objects.nonNull(usage.activity()))
            .map(usage -> {
                if(usage.roomSet() != null && usage.roomSet().isPrelim()) {
                    return usage.activity().replaceAll("#\\d+", "Prelim");
                } else {
                    return usage.activity();
                }
            })
            .distinct()
            .collect(Collectors.joining(","));
    }

    private Map<RoomID, List<RoomUsage>> aggregate(Hotel hotel) {
        return hotel
            .roomUsage()
            .stream()
            .collect(Collectors.groupingBy(RoomUsage::key));
    }
}
