package org.ncfl.specs;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

class MyProblemHandler extends DeserializationProblemHandler {
    @Override
    public Object handleWeirdStringValue(DeserializationContext ctx,
                                         Class<?> targetType,
                                         String valueToConvert,
                                         String failureMsg) throws IOException {
        if ((targetType == Boolean.class || targetType == boolean.class) && ("Yes".equalsIgnoreCase(
            valueToConvert))) {
            return true;
        }
        if (targetType == DayOfWeek.class && valueToConvert != null && !valueToConvert.isEmpty()) {
            return DayOfWeek.valueOf(valueToConvert.toUpperCase());
        }
        if (targetType == LocalDate.class && valueToConvert != null && !valueToConvert.isEmpty()) {
            try {
                return MonthDay.parse(valueToConvert, DateTimeFormatter.ofPattern("MMMM dd")).atYear(2025);
            } catch (DateTimeParseException e) {
                return LocalDate.parse(valueToConvert, DateTimeFormatter.ofPattern("M/dd/yyyy"));
            }
        }
        return super.handleWeirdStringValue(ctx, targetType, valueToConvert, failureMsg);
    }
}
