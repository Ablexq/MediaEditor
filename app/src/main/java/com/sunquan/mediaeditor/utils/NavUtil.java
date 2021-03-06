package com.sunquan.mediaeditor.utils;

import android.content.Context;
import android.content.Intent;

import com.sunquan.mediaeditor.audio.AudioNavActivity;
import com.sunquan.mediaeditor.audio.record.AudioRecordActivity;
import com.sunquan.mediaeditor.photo.PhotoNavActivity;
import com.sunquan.mediaeditor.photo.SurfaceViewActivity;
import com.sunquan.mediaeditor.photo.choose.ChooseMediaActivity;
import com.sunquan.mediaeditor.photo.choose.ChooseMediaWithDirActivity;

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
        Intent i = new Intent(context, AudioNavActivity.class);
        context.startActivity(i);
    }

    public static void gotoVideoNavActivity(Context context) {

    }

    public static void gotoChooseMediaActivity(Context context,int mediaType) {
        Intent i = new Intent(context, ChooseMediaActivity.class);
        i.putExtra(Constants.Key.MEDIA_TYPE,mediaType);
        context.startActivity(i);
    }

    public static void gotoChooseMediaWithDirActivity(Context context, int mediaType) {
        Intent i = new Intent(context, ChooseMediaWithDirActivity.class);
        i.putExtra(Constants.Key.MEDIA_TYPE,mediaType);
        context.startActivity(i);
    }

    public static void gotoSurfaceViewActivity(Context context) {
        Intent i = new Intent(context, SurfaceViewActivity.class);
        context.startActivity(i);
    }

    public static void gotoAudioRecordActivity(Context context) {
        Intent i = new Intent(context, AudioRecordActivity.class);
        context.startActivity(i);
    }
}
