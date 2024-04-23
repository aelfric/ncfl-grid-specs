package org.ncfl.specs;

import j2html.TagCreator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static j2html.TagCreator.ul;

public class ScheduleReport implements Reporter {
    @Override
    public String process(List<Hotel> hotelList) {
        return ul()
            .with(hotelList
                .stream()
                .flatMap(h -> h.roomUsage().stream())
                .flatMap(usage -> {
                    var activity = Objects.requireNonNullElse(usage.activity(), "");
                    return Stream.of(activity.split(","))
                        .map(a -> usage.day() + " " + usage.start() + " " + usage.end() + " " + a);
                })
                .map(activity -> activity.replaceAll("#\\d+", "Prelim"))
                .distinct()
                .sorted()
                .map(TagCreator::li)).render();
    }
}
