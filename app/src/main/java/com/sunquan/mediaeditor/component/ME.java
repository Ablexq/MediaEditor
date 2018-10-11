package com.sunquan.mediaeditor.component;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.sunquan.mediaeditor.general.RequestPermissionActivity;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/9
 **/
public class ME {
    public static MediaChooseComponent media(Context context) {
        return MediaChooseComponent.getIns(context);
    }

    public static void requestPermission(Context context, String[] permissionList, RequestPermissionActivity.Callback callback) {
        if (permissionList == null || permissionList.length == 0) {
            return;
        }
        for (int i = 0; i < permissionList.length; i++) {
            if (ActivityCompat.checkSelfPermission(context, permissionList[i]) != PackageManager.PERMISSION_GRANTED) {
                Intent intent = RequestPermissionActivity.newIntent(context, permissionList, callback);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
}
