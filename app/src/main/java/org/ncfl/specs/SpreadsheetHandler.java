package org.ncfl.specs;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.StreamSupport;

@Singleton
public class SpreadsheetHandler {
    protected static final Logger logger = LogManager.getLogger();
    ObjectMapper objectMapper;

    @Inject
    public SpreadsheetHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Hotel> getHotelRoomUsage(File file) {
        try (
            InputStream inputStream = new FileInputStream(file);
            Workbook wb = WorkbookFactory.create(inputStream)
        ) {
            return List.of(
                getRoomUsages("Palmer House", wb),
                getRoomUsages("Hilton Chicago", wb)
            );
        } catch (IOException e) {
            RoomSpecReport.logger.error("Could not read input file", e);
            throw new RuntimeException("Could not read input file");
        }
    }

    private Hotel getRoomUsages(String hotelName, Workbook wb) {
        final Sheet sheet = wb.getSheet(hotelName);
        List<String> headers = slurpHeaders(sheet.getRow(0));
        final List<RoomUsage> roomUsages = StreamSupport.stream(sheet.spliterator(), true)
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
                    logger.error("Could not read row {}", datum, e);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .toList();
        return new Hotel(hotelName, roomUsages);
    }

    public List<String> slurpHeaders(Row row) {
        List<String> headers = new ArrayList<>();
        for (Cell cell : row) {
            headers.add(cell.toString());
        }
        return Collections.unmodifiableList(headers);
    }


    private Map<String, String> slurpRow(Row row, List<String> headers) {
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

}
