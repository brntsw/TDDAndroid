package com.techyourchance.unittestingfundamentals.exercise2;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StringDuplicatorTest {
    
    private StringDuplicator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new StringDuplicator();
    }

    @Test
    public void duplicate_emptyString_emptyStringReturned() {
        String result = SUT.duplicate("");
        assertThat(result, is(""));
    }

    @Test
    public void duplicate_chararacter_duplicatedCharacterReturned() {
        String result = SUT.duplicate("a");
        assertThat(result, is("aa"));
    }

    @Test
    public void duplicate_longString_duplicatedStringReturned() {
        String result = SUT.duplicate("Hello");
        assertThat(result, is("HelloHello"));
    }
}