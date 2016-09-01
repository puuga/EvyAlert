package com.appspace.evyalert.util;

import android.net.Uri;

/**
 * Created by siwaweswongcharoen on 9/1/2016 AD.
 */
public class FirebaseStorageUtil {
    public static String getMediaDownloadUrl(Uri downloadUrl) {
        return "https://firebasestorage.googleapis.com"
                + downloadUrl.getEncodedPath() + "?alt=media";
    }
}
