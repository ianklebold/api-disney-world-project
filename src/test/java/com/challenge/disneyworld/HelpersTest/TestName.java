package com.challenge.disneyworld.HelpersTest;

import static org.junit.Assert.*;

import com.challenge.disneyworld.utils.helpers.*;

import org.junit.Test;

public class TestName {
    @Test
    public void return_true_when_the_name_is_valid(){
        
        assertTrue(Helpers.controlRegexName("a"));
        assertTrue(Helpers.controlRegexName("g"));
        assertTrue(Helpers.controlRegexName("z"));
    }

    @Test
    public void return_true_when_the_name_have_more_than_one_letter(){
        
        assertTrue(Helpers.controlRegexName("aasdas"));
        assertTrue(Helpers.controlRegexName("g"));
        assertTrue(Helpers.controlRegexName("zpksa"));
    }

    @Test
    public void return_true_when_the_name_have_more_than_one_letter_whit_uppercase_letters(){
        
        assertTrue(Helpers.controlRegexName("aasdasASDASD"));
        assertTrue(Helpers.controlRegexName("gASDADSasaa"));
        assertTrue(Helpers.controlRegexName("zpksa"));
    }

}
