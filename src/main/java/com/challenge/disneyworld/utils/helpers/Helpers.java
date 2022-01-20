package com.challenge.disneyworld.utils.helpers;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Helpers {
    

    public static Boolean controlDate(String date){
        date = date.trim().replaceAll("\\s+","");
        try {
            LocalDate.parse(date); 
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean controlRegexName(String name){
        Pattern NAME_PATTERN = Pattern.compile("[[a-z]|[A-Z]]+");
        return NAME_PATTERN.matcher(name.trim().replaceAll("\\s+","")).matches();
    }

}
