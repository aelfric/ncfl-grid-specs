package org.ncfl.specs;

import java.util.List;

public record Hotel(String name, List<RoomUsage> roomUsage) {
}
