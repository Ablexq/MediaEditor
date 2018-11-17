package com.sunquan.mediaeditor.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunquan.mediaeditor.R;
import com.sunquan.mediaeditor.utils.StatusBarUtil;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/9
 **/
public class TitleBar extends LinearLayout {
    TextView back;
    TextView confirm;
    TextView title;
    String titleStr;
    String right;
    private Context context;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        inflate(context, R.layout.view_titlebar, this);
        if (attrs != null) {
            getAttrs(context, attrs);
        }
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        titleStr = ta.getString(R.styleable.TitleBar_s_title);
        right = ta.getString(R.styleable.TitleBar_s_right);
        initView();
        bindAction();
    }

    private void bindAction() {
        back.setOnClickListener(v -> {
            if (listener != null) {
                listener.clickBack();
            }
        });
        confirm.setOnClickListener(v -> {
            if (listener != null) {
                listener.clickConfirm();
            }
        });
    }

    private void initView() {
        back = findViewById(R.id.back);
        confirm = findViewById(R.id.confirm);
        title = findViewById(R.id.title);
        if (!TextUtils.isEmpty(titleStr)) {
            title.setText(titleStr);
            title.setVisibility(VISIBLE);
        } else {
            title.setVisibility(GONE);
        }
        title.getPaint().setFakeBoldText(true);

        if (!TextUtils.isEmpty(right)) {
            confirm.setText(right);
            confirm.setVisibility(VISIBLE);
        } else {
            confirm.setVisibility(GONE);
        }
        confirm.getPaint().setFakeBoldText(true);
    }

    public void setOnTitleBarClickListener(OnTitleBarClickListener listener) {
        this.listener = listener;
    }

    private OnTitleBarClickListener listener;

    public abstract static class OnTitleBarClickListener {
        public void clickBack(){

        }

        public void clickConfirm(){

        }
    }
}
