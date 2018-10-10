package com.sunquan.mediaeditor.general;

import android.os.Bundle;
import android.widget.TextView;

import com.sunquan.mediaeditor.R;
import com.sunquan.mediaeditor.utils.NavUtil;

public class MainActivity extends BaseActivity {
    TextView photo;
    TextView audio;
    TextView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        photo = findViewById(R.id.photo);
        audio = findViewById(R.id.audio);
        video = findViewById(R.id.video);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void bindAction() {
        photo.setOnClickListener(v->NavUtil.gotoPhotoNavActivity(this));
        audio.setOnClickListener(v->NavUtil.gotoAudioNavActivity(this));
        video.setOnClickListener(v->NavUtil.gotoVideoNavActivity(this));
    }
}
