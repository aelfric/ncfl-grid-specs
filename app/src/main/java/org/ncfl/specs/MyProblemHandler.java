package org.ncfl.specs;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;

import java.io.IOException;
import java.time.DayOfWeek;

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
        return super.handleWeirdStringValue(ctx, targetType, valueToConvert, failureMsg);
    }
}
