/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.ncfl.specs;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import j2html.tags.DomContent;
import j2html.tags.specialized.LiTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static j2html.TagCreator.*;


public class App {
    protected static final Logger logger = LogManager.getLogger();

    static ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .addHandler(new MyProblemHandler());

    public static void main(String[] args) {
        slurp(Paths.get("/home/fricc/Dropbox/4N6/NCFL 2024 Competition Space Grid.xlsx"));
    }

    public static void slurp(Path inputFile) {
        try (
            InputStream inputStream = new FileInputStream(inputFile.toFile());
            Workbook wb = WorkbookFactory.create(inputStream)
        ) {

            Files.writeString(Paths.get("output.html"),
                body(h1("Hilton Chicago"))
                    .with(slurp(wb.getSheet("Hilton Chicago")))
                    .with(h1("Palmer House"))
                    .with(slurp(wb.getSheet("Palmer House")))
                    .render(),
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE
            );
        } catch (IOException e) {
            logger.error("Could not read input file", e);
        }
    }

    public static List<String> slurpHeaders(Row row) {
        List<String> headers = new ArrayList<>();
        for (Cell cell : row) {
            headers.add(cell.toString());
        }
        return Collections.unmodifiableList(headers);
    }

    public static List<DomContent> slurp(Sheet sheet) {
        List<String> headers = slurpHeaders(sheet.getRow(0));
        List<RoomUsage> data = StreamSupport.stream(sheet.spliterator(), true)
            .skip(1)
            .map(row -> {
                Map<String, String> datum = slurpRow(row, headers);
                try {
                    if (!datum.getOrDefault("Day", "").isEmpty()) {
                        return objectMapper.convertValue(datum, RoomUsage.class);
                    } else {
                        return null;
                    }
                } catch (IllegalArgumentException e) {
                    logger.error("Could not read row", e);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .filter(roomUsage -> roomUsage.start() != null && roomUsage.activity() != null)
            .toList();

        return List.of(
            filterAndPrintRooms("Saturday Room Sets", data, DayOfWeek.SATURDAY),
            filterAndPrintRooms("Sunday Room Sets", data, DayOfWeek.SUNDAY)
        );
    }

    private static DomContent filterAndPrintRooms(String title,
                                                  List<RoomUsage> data,
                                                  DayOfWeek dayOfWeek) {
        Map<RoomID, List<RoomUsage>> saturdayRoomMap = data
            .stream()
            .filter(u -> u.day() == dayOfWeek)
            .collect(Collectors.groupingBy(RoomUsage::key));

        return section(h2(title))
            .with(printRoomUsages(saturdayRoomMap));
    }

    private static Map<String, String> slurpRow(Row row, List<String> headers) {
        Map<String, String> datum = new HashMap<>();
        for (Cell cell : row) {
            String key = headers.get(cell.getColumnIndex());
            if (!key.isEmpty()) {

                switch (cell.getCellType()) {
                    case STRING -> datum.put(key, cell.getStringCellValue());
                    case NUMERIC -> {
                        if (DateUtil.isCellDateFormatted(cell)) {
                            datum.put(key,
                                DateUtil.getLocalDateTime(cell.getNumericCellValue())
                                    .toString());
                        } else {
                            datum.put(key,
                                Double.toString(cell.getNumericCellValue()));
                        }
                    }
                    case BLANK -> datum.put(key, "");
                    case BOOLEAN -> datum.put(key, Boolean.toString(cell.getBooleanCellValue()));
                    default -> logger.warn("ignoring cell {}", cell);
                }
            }
        }
        return datum;
    }

    private static Stream<DomContent> printRoomUsages(Map<RoomID, List<RoomUsage>> roomMap) {
        return roomMap
            .entrySet()
            .stream()
            .sorted(Comparator.comparing(
                (Map.Entry<RoomID, List<RoomUsage>> roomIDListEntry) ->
                    roomIDListEntry.getKey().name()))
            .map(App::roomUsageDiv);
    }

    private static DomContent roomUsageDiv(Map.Entry<RoomID, List<RoomUsage>> entry) {
        return div(h3(entry.getKey().name()))
            .with(
                ul()
                    .with(
                        entry
                            .getValue()
                            .stream()
                            .map(roomUsage -> {
                                final LiTag li = li(
                                    span("[%s - %s] ".formatted(roomUsage.start(), roomUsage.end())),
                                    text(roomUsage.roomSet()),
                                    text(" ("),
                                    text(roomUsage.activity()),
                                    text(")")
                                );
                                if(roomUsage.notes()!=null){
                                    return li.with(
                                        br(),
                                        i(roomUsage.notes())
                                    );
                                } else {
                                    return li;
                                }
                            })
                    )
            );
    }

    static class MyProblemHandler extends DeserializationProblemHandler {
        @Override
        public Object handleWeirdStringValue(DeserializationContext ctx,
                                             Class<?> targetType,
                                             String valueToConvert,
                                             String failureMsg) throws IOException {
            if ((targetType == Boolean.class || targetType == boolean.class) && ("Yes".equalsIgnoreCase(valueToConvert))) {
                return true;
            }
            if (targetType == DayOfWeek.class && valueToConvert != null && !valueToConvert.isEmpty()) {
                return DayOfWeek.valueOf(valueToConvert.toUpperCase());
            }
            return super.handleWeirdStringValue(ctx, targetType, valueToConvert, failureMsg);
        }
    }

}
