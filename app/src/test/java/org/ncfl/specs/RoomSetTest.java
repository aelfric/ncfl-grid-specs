package org.ncfl.specs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoomSetTest {

    public static Stream<Arguments> getEnums() {
        return Arrays
            .stream(RoomSet.values())
            .filter(rs -> rs != RoomSet.SPECIAL_OTHER)
            .map(rs -> Arguments.of(rs, rs.title()));
    }


    ObjectMapper objectMapper = new ObjectMapper();
    @ParameterizedTest
    @MethodSource("getEnums")
    void testDeserialization(RoomSet expected, String title) {
        final RoomSet roomSet = objectMapper.convertValue(title, RoomSet.class);
        assertEquals(expected, roomSet);
    }

}
