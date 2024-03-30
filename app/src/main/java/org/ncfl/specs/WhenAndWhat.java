package org.ncfl.specs;

import java.time.LocalTime;

public record WhenAndWhat(
    LocalTime start,
    LocalTime end,
    RoomSet roomSet

) {
    public static WhenAndWhat fromRoomUsage(RoomUsage roomUsage) {
        return new WhenAndWhat(
            roomUsage.start(),
            roomUsage.end(),
            roomUsage.roomSet()
        );
    }
}
