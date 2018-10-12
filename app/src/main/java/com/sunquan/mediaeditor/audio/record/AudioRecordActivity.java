package com.sunquan.mediaeditor.audio.record;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sunquan.mediaeditor.R;
import com.sunquan.mediaeditor.audio.AudioRecorder;
import com.sunquan.mediaeditor.general.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/12
 **/
public class AudioRecordActivity extends BaseActivity {
    private static final int UPDATE_RECORD_TIME = 1;
    AudioRecorder audioRecorder;
    @BindView(R.id.start_record)
    TextView startRecord;
    @BindView(R.id.stop_record)
    TextView stopRecord;
    @BindView(R.id.pause_record)
    TextView pauseRecord;
    @BindView(R.id.pcm)
    TextView pcm;
    @BindView(R.id.wav)
    TextView wav;
    @BindView(R.id.time)
    TextView time;

    private long startTime;
    private long curTime = 0;

    private Handler timeHandle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_RECORD_TIME:
                    curTime = curTime + System.currentTimeMillis() - startTime;
                    time.setText(curTime / 1000.0f + "");
                    break;
            }
        }
    };

    private Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            timeHandle.obtainMessage(UPDATE_RECORD_TIME).sendToTarget();
            timeHandle.postDelayed(updateTimeTask, 30);
        }
    };


    @Override
    public void initView() {
        audioRecorder = AudioRecorder.getIns(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_audio_record;
    }

    @Override
    public void bindAction() {
        startRecord.setOnClickListener(v -> startRecord());
        stopRecord.setOnClickListener(v -> stopRecord());
        pauseRecord.setOnClickListener(v -> pauseRecord());
        pcm.setOnClickListener(v -> gotoPCMFileList());
        wav.setOnClickListener(v -> gotoWAVFileList());
    }

    private void gotoPCMFileList() {

    }

    private void gotoWAVFileList() {

    }

    private void startRecord() {
        try {
            if (audioRecorder.getStatus() == AudioRecorder.Status.STATUS_NO_READY) {
                String fileName = "audio_" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
                audioRecorder.init(fileName);
                startTime = System.currentTimeMillis();
                curTime = 0;
                time.setText(curTime + "");
                timeHandle.post(updateTimeTask);
                audioRecorder.startRecording();


                startRecord.setVisibility(View.GONE);
                pauseRecord.setVisibility(View.VISIBLE);
                stopRecord.setVisibility(View.VISIBLE);
            }
        } catch (Exception o) {
            Toast.makeText(this, o.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecord() {
        try {
            //停止录音
            audioRecorder.stopRecording();
            timeHandle.removeCallbacks(updateTimeTask);
            startRecord.setVisibility(View.VISIBLE);
            stopRecord.setVisibility(View.GONE);
            pauseRecord.setVisibility(View.GONE);
        } catch (Exception o) {
            Toast.makeText(this, o.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void pauseRecord() {
        try {
            if (audioRecorder.getStatus() == AudioRecorder.Status.STATUS_START) {
                //暂停录音
                timeHandle.removeCallbacks(updateTimeTask);
                audioRecorder.pauseRecording();
                pauseRecord.setText("continue record");
            } else {
                audioRecorder.startRecording();
                startTime = System.currentTimeMillis();
                timeHandle.post(updateTimeTask);
                pauseRecord.setText("pause record");
            }
        } catch (IllegalStateException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (audioRecorder.getStatus() == AudioRecorder.Status.STATUS_START) {
            audioRecorder.pauseRecording();
            pauseRecord.setText("继续录音");
        }

    }

    @Override
    protected void onDestroy() {
        audioRecorder.release();
        super.onDestroy();

    }
}
