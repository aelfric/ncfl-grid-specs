package org.ncfl.specs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public record RoomUsage(
    @JsonProperty("Day") DayOfWeek day,
    @JsonProperty("Date") LocalDate date,
    @JsonProperty("Venue") String venue,
    @JsonProperty("REB3 Updated") String updated,
    @JsonProperty("Hotel Floor") String floor,
    @JsonProperty("Room Name/Number") String name,
    @JsonProperty("NCFL Activity") String activity,
    @JsonProperty("Start") LocalTime start,
    @JsonProperty("End") LocalTime end,
    @JsonProperty("Publish") boolean publish,
    @JsonProperty("Room Set") RoomSet roomSet,
    @JsonProperty("A/V Needs") String avNeeds,
    @JsonProperty("Catering ?") String catering,
    @JsonProperty("Room Turn ?") String isRoomTurn, // TODO should be boolean
    @JsonProperty("Hotel Notes") String hotelNotes,
    @JsonProperty("Internal Notes") String internalNotes
) {
    public RoomID key() {
        return new RoomID(venue, floor, name);
    }
}
