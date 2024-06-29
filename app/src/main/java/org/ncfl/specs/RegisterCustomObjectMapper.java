package org.ncfl.specs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.*;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;

@Singleton
public class RegisterCustomObjectMapper implements ObjectMapperCustomizer {
    public static final DateTimeFormatter LOCAL_TIME;
    static {
        LOCAL_TIME = new DateTimeFormatterBuilder()
            .appendValue(CLOCK_HOUR_OF_AMPM, 1, 2, SignStyle.NEVER)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .optionalStart()
            .appendLiteral(':')
            .appendValue(SECOND_OF_MINUTE, 2)
            .optionalStart()
            .appendFraction(NANO_OF_SECOND, 0, 9, true)
            .optionalEnd()
            .optionalEnd()
            .appendLiteral(" ")
            .appendText(AMPM_OF_DAY)
            .toFormatter();
    }

    @Override
    public void customize(ObjectMapper objectMapper) {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(LOCAL_TIME));

        objectMapper
            .registerModule(javaTimeModule)
            .addHandler(new MyProblemHandler())
            .coercionConfigDefaults()
            .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsEmpty);
    }
}
