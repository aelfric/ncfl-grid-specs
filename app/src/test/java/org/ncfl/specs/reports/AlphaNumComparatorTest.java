package org.ncfl.specs.reports;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.*;

class AlphaNumComparatorTest {
    Comparator<String> comparator =  AlphaNumComparator.ALPHANUM;

    @SuppressWarnings("EqualsWithItself")
    @Test
    void testCompare() {
        assertThat(comparator.compare("A", "A"), comparesEqualTo(0));
    }

    @Test
    void testCompare2() {
        assertThat(comparator.compare("B", "A"), greaterThan(0));
    }

    @Test
    void testCompare3() {
        assertThat(comparator.compare("B", "A"), greaterThan(0));
    }

    @Test
    void testCompare4() {
        assertThat(comparator.compare("Chase 10", "Chase 2"), greaterThan(0));
    }

}
