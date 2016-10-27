package com.appspace.evyalerts.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

/**
 * Created by siwaweswongcharoen on 9/22/2016 AD.
 */

public class ViewBlinkingUtil {
    public static void blinking(TextView textView) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(5);
        textView.startAnimation(anim);
    }
}
