package org.ncfl.specs.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.ncfl.specs.RegisterCustomObjectMapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class RoomUsageTest {
    String json = """
        {"NCFL Activity":"Congress Meeting Room","A/V Needs":"Table Microphone","Venue":"Hilton Chicago","Start":"7:00 AM","Room Name/Number":"Williford C","Room Turn ?":"","Hotel Notes":"","Hotel Floor":"3","Date":"May 26","Room Set":"Debate - Elim","Internal Notes":"Set as Debate Elim to prep for that without a turn","Catering ?":"","End":"7:30 AM","Day":"Sunday","REB3 Updated":"","Publish":""}
        """;

    @Test
    void testDeserialize() throws JsonProcessingException {
        final RegisterCustomObjectMapper registerCustomObjectMapper = new RegisterCustomObjectMapper();
        final ObjectMapper objectMapper = new ObjectMapper();
        registerCustomObjectMapper.customize(objectMapper);

        final RoomUsage roomUsage = objectMapper.readValue(json, RoomUsage.class);

        assertThat(roomUsage.start().getHour(), equalTo(7));
        assertThat(roomUsage.date().getYear(), equalTo(2024));

    }
}
