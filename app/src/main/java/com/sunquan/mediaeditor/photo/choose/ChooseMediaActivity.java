package com.sunquan.mediaeditor.photo.choose;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.sunquan.mediaeditor.R;
import com.sunquan.mediaeditor.general.BaseActivity;
import com.sunquan.mediaeditor.utils.Constants;
import com.sunquan.mediaeditor.utils.ViewUtil;
import com.sunquan.mediaeditor.view.TitleBar;

import butterknife.BindView;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/9
 **/
public class ChooseMediaActivity extends BaseActivity {
    public static final int GRID_COLUMS = 3;
    @BindView(R.id.titlebar)
    private TitleBar titleBar;
    @BindView(R.id.recycleView)
    private RecyclerView recyclerView;
    private MediaChooseAdapter adapter;
    private int mediaType;

    @Override
    public void initView() {
        mediaType = getIntent().getIntExtra(Constants.Key.MEDIA_TYPE, Constants.MediaType.MEDIA_PHOTO);
        adapter = new MediaChooseAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, ChooseMediaActivity.GRID_COLUMS));
        recyclerView.addItemDecoration(new MediaChooseItemDecoration(ChooseMediaActivity.GRID_COLUMS, ViewUtil.dp2px(this, 1), false));
        recyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter.loadData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_media;
    }

    @Override
    public void bindAction() {
        titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void clickBack() {
                finish();
            }
        });
    }
}
