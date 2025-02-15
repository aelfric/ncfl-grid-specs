package org.ncfl.specs.reports;

import org.ncfl.specs.model.Hotel;
import org.ncfl.specs.model.RoomSet;

import java.util.Arrays;
import java.util.List;

import static j2html.TagCreator.*;

public class RoomSetReport implements Reporter {
    @Override
    public String process(List<Hotel> hotelList) {
        return ul().with(
            Arrays.stream(RoomSet.values())
                .map(rs -> li(a(rs.name())
                    .withHref(rs.href())
                    .withTarget("_blank")
                    .withRel("noopener noreferrer")
                ))
        ).toString();
    }
}
