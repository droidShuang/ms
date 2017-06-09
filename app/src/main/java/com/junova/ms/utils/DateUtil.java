package com.junova.ms.utils;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by junova on 2017-03-21.
 */

public class DateUtil {
    public static String getTimestamp() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
