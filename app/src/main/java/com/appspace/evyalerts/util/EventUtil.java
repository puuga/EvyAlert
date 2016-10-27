package com.appspace.evyalerts.util;

import android.text.TextUtils;

import com.appspace.appspacelibrary.util.LoggerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siwaweswongcharoen on 9/1/2016 AD.
 */
public class EventUtil {
    public static String makeEventFilterString() {
        List<String> list = new ArrayList<>();
        if (DataStoreUtils.getInstance().isAccidentSwitchOn())
            list.add("0");
        if (DataStoreUtils.getInstance().isNaturalDisasterSwitchOn())
            list.add("1");
        if (DataStoreUtils.getInstance().isOtherSwitchOn())
            list.add("2");
        if (DataStoreUtils.getInstance().isTrafficJamSwitchOn())
            list.add("3");
        String output = TextUtils.join(",", list);
        LoggerUtils.log2D("EventUtil", "makeEventFilterString: " + output);
        return output;
    }
}
