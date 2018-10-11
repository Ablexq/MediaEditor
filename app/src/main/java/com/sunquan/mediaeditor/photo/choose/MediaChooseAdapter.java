package com.sunquan.mediaeditor.photo.choose;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sunquan.mediaeditor.R;
import com.sunquan.mediaeditor.component.ME;
import com.sunquan.mediaeditor.model.MediaItem;
import com.sunquan.mediaeditor.utils.FrescoUtil;
import com.sunquan.mediaeditor.utils.MathUtil;
import com.sunquan.mediaeditor.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/10
 **/
public class MediaChooseAdapter extends RecyclerView.Adapter {
    private Context context;
    private int mItemSize;
    private List<MediaItem> items = new ArrayList<>();


    public MediaChooseAdapter(Context context) {
        this.context = context;
        this.mItemSize = (ViewUtil.getScreenWidth(context) - (ChooseMediaActivity.GRID_COLUMS - 1) * ViewUtil.dp2px(context, 1)) / ChooseMediaActivity.GRID_COLUMS;
        ME.media(context).setOnMediaChangeListener(mediaInfo -> {
            if (!isExist(mediaInfo)) {
                ((Activity) context).runOnUiThread(() -> {
                    items.add(mediaInfo);
                    notifyItemInserted(items.size() - 1);
                });
            }
        });
    }

    private boolean isExist(MediaItem mediaInfo) {
        for (int i = 0; i < items.size(); i++) {
            if (!TextUtils.isEmpty(mediaInfo.filePath)
                    && !TextUtils.isEmpty(items.get(i).filePath)
                    && mediaInfo.filePath.equals(items.get(i).filePath)) {
                return true;
            }
        }
        return false;
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

    public void loadData(String dir) {
        items.clear();
        notifyDataSetChanged();
        ME.media(context).loadImageMedias(dir);
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

        public void bind(MediaItem item, int position, MediaVH holder) {
            resizeView(rootView);
            if (item == null) {
                return;
            }
            FrescoUtil.loadImage(image, item.thumbnailPath);
            FrescoUtil.loadImage(image, item.filePath);
            if (item.type == MediaItem.TYPE_VIDEO) {
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

        private void bindForImage(MediaItem item) {
            videoDuration.setVisibility(View.GONE);
        }

        private void bindForVideo(MediaItem item) {
            videoDuration.setVisibility(View.VISIBLE);
            MathUtil.getVideoDur(videoDuration, item.duration);
        }
    }
}
