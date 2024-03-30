package org.ncfl.specs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;

@Singleton
public class RegisterCustomObjectMapper implements ObjectMapperCustomizer {
    @Override
    public void customize(ObjectMapper objectMapper) {
        objectMapper
            .registerModule(new JavaTimeModule())
            .addHandler(new MyProblemHandler())
            .coercionConfigDefaults()
            .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsEmpty);
    }
}
