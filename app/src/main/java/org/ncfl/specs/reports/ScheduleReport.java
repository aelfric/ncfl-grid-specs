package org.ncfl.specs.reports;

import org.ncfl.specs.model.Hotel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class ScheduleReport {
    public List<ScheduleBlock> process(List<Hotel> hotelList) {
//        return ul()
//            .with(
               return hotelList
                    .stream()
                    .flatMap(h -> h.roomUsage().stream())
                    .filter(u -> Objects.nonNull(u.activity()) && !u.activity().isEmpty())
                    .flatMap(usage ->
                        Stream.of(usage.activity().split(","))
                            .map(a ->
                            {
                                String activityDescription = usage.activity().replaceAll("#\\d+", "Prelim");
                                return new ScheduleBlock(
                                    usage.date().atTime(usage.start()),
                                    usage.date().atTime(usage.end()),
                                    UUID.nameUUIDFromBytes((activityDescription + usage.start()).getBytes()),
                                    activityDescription);
                            })
                            .distinct()
                    )
                   .distinct()
                   .toList();
    }

    public record ScheduleBlock(
        LocalDateTime start,
        LocalDateTime end,
        UUID id,
        String text
    ) {

    }
}
