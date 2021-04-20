package com.techyourchance.unittestingfundamentals.exercise3;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IntervalsAdjacencyDetectorTest {

    private IntervalsAdjacencyDetector SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new IntervalsAdjacencyDetector();
    }

    // IntervalA is before and adjacent to IntervalB
    @Test
    public void intervalAdjacency_intervalAIsBeforeAndAdjacentToIntervalB_trueReturned() {
        Interval intervalA = new Interval(0, 3);
        Interval intervalB = new Interval(3, 6);
        boolean result = SUT.isAdjacent(intervalA, intervalB);
        assertThat(result, is(true));
    }

    // IntervalA overlaps IntervalB at the start
    @Test
    public void intervalAdjacency_intervalAOverlapsIntervalBAtTheStart_falseReturned() {
        Interval intervalA = new Interval(2, 4);
        Interval intervalB = new Interval(1, 8);
        boolean result = SUT.isAdjacent(intervalA, intervalB);
        assertThat(result, is(false));
    }

    //IntervalA overlaps IntervalB at the end
    @Test
    public void intervalAdjacency_intervalAOverlapsIntervalBAtTheEnd_falseReturned() {
        Interval intervalA = new Interval(5, 8);
        Interval intervalB = new Interval(3, 6);
        boolean result = SUT.isAdjacent(intervalA, intervalB);
        assertThat(result, is(false));
    }

    // IntervalA is after and adjacent to IntervalB
    @Test
    public void intervalAdjacency_intervalAIsAfterAndAdjacentIntervalB_trueReturned() {
        Interval intervalA = new Interval(7, 9);
        Interval intervalB = new Interval(4, 7);
        boolean result = SUT.isAdjacent(intervalA, intervalB);
        assertThat(result, is(true));
    }

    // IntervalA is within IntervalB
    @Test
    public void intervalAdjacency_intervalAIsWithinIntervalB_falseReturned() {
        Interval intervalA = new Interval(3, 6);
        Interval intervalB = new Interval(2, 7);
        boolean result = SUT.isAdjacent(intervalA, intervalB);
        assertThat(result, is(false));
    }

    //IntervalA contains IntervalB
    @Test
    public void intervalAdjacency_intervalAContainsIntervalB_falseReturned() {
        Interval intervalA = new Interval(-1, 4);
        Interval intervalB = new Interval(0, 2);
        boolean result = SUT.isAdjacent(intervalA, intervalB);
        assertThat(result, is(false));
    }

    // IntervalA is out of IntervalB
    @Test
    public void intervalAdjacency_outOfInterval_falseReturned() {
        Interval intervalA = new Interval(4, 6);
        Interval intervalB = new Interval(1, 3);
        boolean result = SUT.isAdjacent(intervalA, intervalB);
        assertThat(result, is(false));
    }

    //IntervalA is the same as Interval B (they are the same, not adjacent)
    @Test
    public void intervalAdjacency_bothAdjacent_falseReturned() {
        Interval intervalA = new Interval(3, 7);
        Interval intervalB = new Interval(3, 7);
        boolean result = SUT.isAdjacent(intervalA, intervalB);
        assertThat(result, is(false));
    }
}