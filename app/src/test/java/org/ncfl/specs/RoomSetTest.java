package org.ncfl.specs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import j2html.tags.specialized.UlTag;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static j2html.TagCreator.li;
import static org.hamcrest.Matchers.equalTo;
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

    @ParameterizedTest(name = "{0} has a slide linked")
    @MethodSource("getEnums")
    void testAllRoomSetsHaveLinks(RoomSet s){
        assertNotNull(s.href());
    }

    @Test
    void testDescriptionsWithNotes(){
        MatcherAssert.assertThat(
            RoomSet.SPECIAL_SPEECH_WORKROOM.description().with(li("test")).toString(),
            equalTo(RoomSet.SPECIAL_SPEECH_WORKROOM.description().with(li("test")).toString())
        );
    }
}
