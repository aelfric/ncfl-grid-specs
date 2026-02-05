package org.ncfl.specs.reports;

import org.ncfl.specs.model.Hotel;
import org.ncfl.specs.model.RoomSet;

import java.util.Arrays;
import java.util.List;

import static j2html.TagCreator.*;

public class RoomSetReport implements Reporter {
    @Override
    public String process(List<Hotel> hotelList) {
        return table().with(
            Arrays.stream(RoomSet.values())
                .map(rs -> tr(
                    td(a(rs.title())),
                    td(a(rs.href())),
                    td(rs.description())
                ))
        ).toString();
    }
}
