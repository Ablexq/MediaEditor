package com.sunquan.mediaeditor.general;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sunquan.mediaeditor.utils.StatusBarUtil;

import butterknife.ButterKnife;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/9
 **/
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView();
        bindAction();
    }

    public abstract void initView();

    public abstract int getLayoutId();

    public abstract void bindAction();
}
