package org.ncfl.specs.model;

import java.util.List;

public record Hotel(String name, List<RoomUsage> roomUsage) {
    public String slug(){
        return name.toLowerCase().replace(" ", "-");
    }
}
