package com.sunquan.mediaeditor.photo;

import android.widget.TextView;

import com.sunquan.mediaeditor.R;
import com.sunquan.mediaeditor.general.BaseActivity;
import com.sunquan.mediaeditor.utils.Constants;
import com.sunquan.mediaeditor.utils.NavUtil;

import butterknife.BindView;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/9
 **/
public class PhotoNavActivity extends BaseActivity{
    @BindView(R.id.choose_photo)
    TextView chooseMedia;
    @BindView(R.id.choose_photo_with_dir)
    TextView chooseMediaWithDir;

    @Override
    public void initView() {
        chooseMedia = findViewById(R.id.choose_photo);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo_nav;
    }

    @Override
    public void bindAction() {
        chooseMedia.setOnClickListener(v-> NavUtil.gotoChooseMediaActivity(this, Constants.MediaType.MEDIA_PHOTO));
        chooseMediaWithDir.setOnClickListener(v-> NavUtil.gotoChooseMediaWithDirActivity(this, Constants.MediaType.MEDIA_PHOTO));
    }
}
