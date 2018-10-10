package com.sunquan.mediaeditor.utils;

import android.widget.TextView;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/10
 **/
public class MathUtil {
    public static void getVideoDur(TextView view, long duration) {
        if (duration == 0) {
            return;
        }

        int sec = Math.round((float) duration / 1000);
        int min = sec / 60;
        sec %= 60;
        view.setText(String.format("%d:%02d", min, sec));
    }
}
