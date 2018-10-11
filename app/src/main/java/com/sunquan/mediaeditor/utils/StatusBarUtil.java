package com.sunquan.mediaeditor.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/9
 **/
public class StatusBarUtil {

    // 获取手机状态栏高度
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    private static boolean checkNull(Window window) {
        if (window == null) {
            return true;
        } else {
            return Build.VERSION.SDK_INT < 19;
        }
    }

    public static void showStatusBar(Activity context) {
        if (!checkNull(context.getWindow())) {
            View decorView = context.getWindow().getDecorView();
            if (decorView != null) {
                int uiOptions = decorView.getSystemUiVisibility() & -5;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }

    public static void showStatusBar(Window window) {
        if (!checkNull(window)) {
            View decorView = window.getDecorView();
            if (decorView != null) {
                int uiOptions = decorView.getSystemUiVisibility() & -5;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }

    public static void setStatusBarColor(Window window, int color) {
        if (!checkNull(window)) {
            showStatusBar(window);
            if (Build.VERSION.SDK_INT >= 21) {
                int uiOptions = window.getDecorView().getSystemUiVisibility();
                uiOptions |= 256;
                uiOptions |= 1024;
                window.getDecorView().setSystemUiVisibility(uiOptions);
                window.setStatusBarColor(color);
            }

        }
    }

    public static void fullAllBar(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View view = window.getDecorView();
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            window.getDecorView().setSystemUiVisibility(flags);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}
