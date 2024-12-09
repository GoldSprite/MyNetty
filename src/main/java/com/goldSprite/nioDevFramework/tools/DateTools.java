package com.goldSprite.nioDevFramework.tools;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTools {
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    static SimpleDateFormat simpleFormat = new SimpleDateFormat("HH:mm:ss:SSS");
    public static String currentDateTime(){
        return "["+LocalDateTime.now().format(dtf)+"] ";
    }
    public static String formatTime(long millis){return simpleFormat.format(millis);}

}
