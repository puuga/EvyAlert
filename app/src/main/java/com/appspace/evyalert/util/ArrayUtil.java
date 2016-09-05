package com.appspace.evyalert.util;

import java.util.Arrays;

/**
 * Created by siwaweswongcharoen on 9/5/2016 AD.
 */
public class ArrayUtil {
    public static <T> T[] append(T[] arr, T element) {
        final int n = arr.length;
        arr = Arrays.copyOf(arr, n + 1);
        arr[n] = element;
        return arr;
    }

}
