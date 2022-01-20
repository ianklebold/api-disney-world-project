package com.challenge.disneyworld.HelpersTest;

import com.challenge.disneyworld.utils.helpers.Helpers;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;

public class TestControlDate {

    @Test
    public void return_true_when_the_date_is_valid(){
        Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
        
        assertTrue(DATE_PATTERN.matcher("1993-01-12").matches());
        assertTrue(DATE_PATTERN.matcher("2050-03-11").matches());
    }


    @Test
    public void return_false_when_the_date_is_not_valid(){
        Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
        
        assertFalse(DATE_PATTERN.matcher("999-01-11").matches());
        assertFalse(DATE_PATTERN.matcher("02-10-1999").matches());
        assertFalse(DATE_PATTERN.matcher("1999/05/12").matches());
    }

    @Test
    public void return_true_when_the_date_is_valid_whit_LocalDate_parse(){
        assertTrue(Helpers.controlDate("2021-08-21            "));
        assertTrue(Helpers.controlDate("    1992-            11-30"));
        assertTrue(Helpers.controlDate("2010  - 11-22"));
    }

    @Test
    public void return_false_when_the_date_is_valid_whit_LocalDate_parse(){
        assertFalse(Helpers.controlDate("2021/08/21"));
        assertFalse(Helpers.controlDate("11992-11-30"));
        assertFalse(Helpers.controlDate("2010-99-22"));
        assertFalse(Helpers.controlDate("2011-02-72"));
        assertFalse(Helpers.controlDate("02-11-1972"));
    }
    
}
