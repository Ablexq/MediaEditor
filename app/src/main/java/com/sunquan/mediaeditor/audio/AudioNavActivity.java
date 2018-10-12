package com.sunquan.mediaeditor.audio;

import android.widget.TextView;

import com.sunquan.mediaeditor.R;
import com.sunquan.mediaeditor.general.BaseActivity;
import com.sunquan.mediaeditor.utils.NavUtil;

import butterknife.BindView;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/12
 **/
public class AudioNavActivity extends BaseActivity {
    @BindView(R.id.audio_record)
    TextView audioRecord;

    @Override
    public void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_audio_nav;
    }

    @Override
    public void bindAction() {
        audioRecord.setOnClickListener(v-> NavUtil.gotoAudioRecordActivity(this));
    }
}
