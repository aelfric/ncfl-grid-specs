package org.ncfl.specs;

import com.fasterxml.jackson.databind.ObjectMapper;
import j2html.tags.DomContent;
import j2html.tags.specialized.BodyTag;
import j2html.tags.specialized.LiTag;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static j2html.TagCreator.*;


@Singleton
public class RoomSpecReport {
    protected static final Logger logger = LogManager.getLogger();
    private final SpreadsheetHandler spreadsheetHandler;

    ObjectMapper objectMapper;

    @Inject
    public RoomSpecReport(ObjectMapper mapper, SpreadsheetHandler spreadsheetHandler) {
        this.objectMapper = mapper;
        this.spreadsheetHandler = spreadsheetHandler;
    }

    public String slurp(File file) {
        final List<Hotel> hotels = spreadsheetHandler.getHotelRoomUsage(file);
        BodyTag body = body();
        for (Hotel hotel : hotels) {
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
        Map<RoomID, List<RoomUsage>> saturdayRoomMap = data
            .stream()
            .filter(u -> u.day() == dayOfWeek)
            .collect(Collectors.groupingBy(RoomUsage::key));

        return section(h2(title))
            .with(printRoomUsages(saturdayRoomMap));
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
