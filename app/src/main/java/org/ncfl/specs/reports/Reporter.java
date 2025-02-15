package org.ncfl.specs.reports;

import org.ncfl.specs.model.Hotel;

import java.util.List;

public interface Reporter {
    String process(List<Hotel> hotelList);
}
