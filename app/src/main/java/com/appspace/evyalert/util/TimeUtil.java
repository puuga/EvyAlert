package com.appspace.evyalert.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by siwaweswongcharoen on 8/10/2016 AD.
 */
public class TimeUtil {
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        return sdfDate.format(now);
    }
}
