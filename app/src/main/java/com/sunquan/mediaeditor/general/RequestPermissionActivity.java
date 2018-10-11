package com.sunquan.mediaeditor.general;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.sunquan.mediaeditor.utils.StatusBarUtil;

public class RequestPermissionActivity extends Activity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String[] inputPermissions;

    public interface Callback {
        void onResult(boolean success);
    }

    private static final String EXTRA_PERMISSIONS = "permissions";
    private static final String EXTRA_MESSENGER = "messenger";

    public static Intent newIntent(Context context, String[] permissions, Callback callback) {
        Intent intent = new Intent(context, RequestPermissionActivity.class);

        Messenger messenger = new Messenger(new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                callback.onResult(msg.arg1 == 1);
            }
        });
        intent.putExtra(EXTRA_MESSENGER, messenger);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.fullAllBar(getWindow());
        inputPermissions = getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions() {
        if (inputPermissions == null || inputPermissions.length == 0) {
            return;
        }
        requestPermissions(inputPermissions, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != PERMISSION_REQUEST_CODE || grantResults.length == 0) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            fail();
            finish();
            return;
        }
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                fail();
                finish();
                return;
            }
        }
        success();
        finish();
    }

    private void fail() {
        Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show();
        Messenger messenger = getIntent().getParcelableExtra(EXTRA_MESSENGER);
        Message message = Message.obtain();
        message.arg1 = 0;
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void success() {
        Messenger messenger = getIntent().getParcelableExtra(EXTRA_MESSENGER);
        Message message = Message.obtain();
        message.arg1 = 1;
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
