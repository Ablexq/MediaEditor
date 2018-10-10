package com.sunquan.mediaeditor.photo.choose;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sunquan.mediaeditor.R;
import com.sunquan.mediaeditor.model.MediaInfo;
import com.sunquan.mediaeditor.utils.FrescoUtil;
import com.sunquan.mediaeditor.utils.MathUtil;
import com.sunquan.mediaeditor.utils.ViewUtil;

import java.util.List;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/10
 **/
public class MediaChooseAdapter extends RecyclerView.Adapter {
    private Context context;
    private int mItemSize;
    private List<MediaInfo> items;


    public MediaChooseAdapter(Context context) {
        this.context = context;
        this.mItemSize = (ViewUtil.getScreenWidth(context) - (ChooseMediaActivity.GRID_COLUMS - 1) * ViewUtil.dp2px(context, 1)) / ChooseMediaActivity.GRID_COLUMS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MediaVH(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MediaVH) {
            ((MediaVH) holder).bind(items.get(position), position, (MediaVH) holder);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void loadData() {

    }

    private class MediaVH extends RecyclerView.ViewHolder {
        View rootView;
        SimpleDraweeView image;
        TextView videoDuration;

        public MediaVH(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media, parent, false));
            rootView = itemView.findViewById(R.id.root_view);
            image = itemView.findViewById(R.id.image);
            videoDuration = itemView.findViewById(R.id.duration);
        }

        public void bind(MediaInfo item, int position, MediaVH holder) {
            resizeView(rootView);
            if (item == null) {
                return;
            }
            FrescoUtil.loadImageLocalUrl(image, item.thumbnailPath);
            FrescoUtil.loadImageLocalUrl(image, item.filePath);
            if (item.type == MediaInfo.TYPE_VIDEO) {
                bindForVideo(item);
            } else {
                bindForImage(item);
            }
        }

        private void resizeView(View view) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (null != params) {
                if (params.width != mItemSize && params.height != mItemSize) {
                    params.width = mItemSize;
                    params.height = mItemSize;
                    view.setLayoutParams(params);
                }
            }

        }

        private void bindForImage(MediaInfo item) {
            videoDuration.setVisibility(View.GONE);
        }

        private void bindForVideo(MediaInfo item) {
            videoDuration.setVisibility(View.VISIBLE);
            MathUtil.getVideoDur(videoDuration, item.duration);
        }
    }
}
