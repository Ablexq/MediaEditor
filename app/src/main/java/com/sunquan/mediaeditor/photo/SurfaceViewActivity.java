package com.sunquan.mediaeditor.photo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sunquan.mediaeditor.R;
import com.sunquan.mediaeditor.general.BaseActivity;

import butterknife.BindView;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/11
 **/
public class SurfaceViewActivity extends BaseActivity {
    @BindView(R.id.surfaceview)
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Thread thread;
    boolean isRun;
    Paint p;

    @Override
    public void initView() {
        surfaceHolder = surfaceView.getHolder();
        initPaint();
        thread = new Thread(() -> {
            int count = 0;
            while (isRun) {
                Canvas c = null;
                try {
                    synchronized (surfaceHolder) {
                        c = surfaceHolder.lockCanvas();
                        c.drawColor(Color.BLACK);
                        c.drawRect(new RectF(100, 50, 1000, 250), p);
                        c.drawText("这是第" + (count++) + "秒", 300, 400, p);
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        });
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                isRun = true;
                thread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                isRun = false;
            }
        });
    }

    private void initPaint() {
        p = new Paint();
        p.setAntiAlias(true);
        p.setTextSize(100);
        p.setColor(Color.WHITE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_surfaceview;
    }

    @Override
    public void bindAction() {

    }
}
