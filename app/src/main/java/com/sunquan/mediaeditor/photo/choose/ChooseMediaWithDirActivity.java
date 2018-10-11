package com.sunquan.mediaeditor.photo.choose;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sunquan.mediaeditor.R;
import com.sunquan.mediaeditor.component.ME;
import com.sunquan.mediaeditor.general.BaseActivity;
import com.sunquan.mediaeditor.utils.Constants;
import com.sunquan.mediaeditor.utils.ViewUtil;
import com.sunquan.mediaeditor.view.TitleBar;

import butterknife.BindView;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/11
 **/
public class ChooseMediaWithDirActivity extends BaseActivity {
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.dir)
    TextView dir;
    @BindView(R.id.titlebar)
    TitleBar titleBar;
    private int mediaType;
    private MediaChooseAdapter adapter;
    private MediaDirAdapter dirAdapter;
    private PopupWindow popupWindow;
    private boolean isShowDir;

    @Override
    public void initView() {
        dir.setText("全部照片");
        mediaType = getIntent().getIntExtra(Constants.Key.MEDIA_TYPE, Constants.MediaType.MEDIA_PHOTO);
        adapter = new MediaChooseAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, ChooseMediaActivity.GRID_COLUMS));
        recyclerView.addItemDecoration(new MediaChooseItemDecoration(ChooseMediaActivity.GRID_COLUMS, ViewUtil.dp2px(this, 1), false));
        recyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter.loadData("");

        RecyclerView rvDir = (RecyclerView) View.inflate(this, R.layout.recycleview, null);
        rvDir.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        dirAdapter = new MediaDirAdapter(this);
        rvDir.setAdapter(dirAdapter);
        dirAdapter.setData(ME.media(this).getDirs());

        popupWindow = new PopupWindow(rvDir,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                this.getResources().getColor(android.R.color.white)));
        popupWindow.setOutsideTouchable(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_media_with_dir;
    }

    @Override
    public void bindAction() {
        titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void clickBack() {
                finish();
            }
        });
        dirAdapter.setOnDirItemClickListener(dir1 -> {
            dir.setText(dir1);
            adapter.loadData(dir1);
        });
        dir.setOnClickListener(v -> {
            if (popupWindow.isShowing()) {
                isShowDir = false;
                popupWindow.dismiss();
            } else {
                isShowDir = true;
                if (Build.VERSION.SDK_INT < 24) {
                    popupWindow.showAsDropDown(dir);
                } else {
                    // 适配 android 7.0
                    int[] location = new int[2];
                    dir.getLocationOnScreen(location);
                    int x = location[0];
                    int y = location[1];
                    popupWindow.showAtLocation(dir, Gravity.NO_GRAVITY, 0, y + dir.getHeight());
                }
            }
            dir.setActivated(isShowDir);
        });
    }
}
