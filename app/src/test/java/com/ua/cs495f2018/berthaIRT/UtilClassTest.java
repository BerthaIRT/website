package com.ua.cs495f2018.berthaIRT;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilClassTest {
    @Test
    public void formatTimeStampTest() {
        String result = Util.formatTimestamp(1543364367969L);
        String expected = "11/27/18 06:19PM";
        assertEquals(expected, result);
    }

    @Test
    public void formatJustTimeTest() {
        String result = Util.formatJustTime(1543364367969L);
        String expected = "06:19PM";
        assertEquals(expected, result);
    }

    @Test
    public void formatDateStampTest() {
        String result = Util.formatDatestamp(1543364367969L);
        String expected = "11/27/18";
        assertEquals(expected, result);
    }
}
