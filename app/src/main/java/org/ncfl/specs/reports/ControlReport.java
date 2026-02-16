package org.ncfl.specs.reports;

import org.ncfl.specs.model.Hotel;

import java.util.List;

public class ControlReport implements Reporter {
    @Override
    public String process(List<Hotel> hotelList) {
//        hotelList
//            .stream()
//            .map(Hotel::roomUsage)
//            .flatMap(List::stream);
        return "";
    }

    record ControlItem(
        String track,
        String activity,
        String site,
        String room
    ) {

    }

    enum Tracks {
        Policy_Debate,
        PF_Debate,
        LD_Debate,
        Congress,
        EXTEMP,
        DEC,
        OO,
        DP,
        OI,
        DUO,
        Mass,
        Party_Prep,
        Party,
        Postings_Prep,
        Postings
    }
}
