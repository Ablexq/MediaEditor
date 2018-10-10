package com.sunquan.mediaeditor.utils;

import android.content.Context;
import android.content.Intent;

import com.sunquan.mediaeditor.photo.PhotoNavActivity;
import com.sunquan.mediaeditor.photo.choose.ChooseMediaActivity;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/9
 **/
public class NavUtil {
    public static void gotoPhotoNavActivity(Context context) {
        Intent i = new Intent(context, PhotoNavActivity.class);
        context.startActivity(i);
    }

    public static void gotoAudioNavActivity(Context context) {

    }

    public static void gotoVideoNavActivity(Context context) {

    }

    public static void gotoChooseMediaActivity(Context context,int mediaType) {
        Intent i = new Intent(context, ChooseMediaActivity.class);
        i.putExtra(Constants.Key.MEDIA_TYPE,mediaType);
        context.startActivity(i);
    }
}
