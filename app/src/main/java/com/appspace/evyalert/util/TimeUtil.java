package com.appspace.evyalert.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by siwaweswongcharoen on 8/10/2016 AD.
 */
public class TimeUtil {
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdfDate.format(now);
    }

    public static long getCurrentTime() {
        return new Date().getTime();
    }

    public static long getLast2DaysTime() {
        return new Date().getTime() - 17280000;
    }

    public static String timpStampFormater(long time) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return sdfDate.format(date);
    }
}
