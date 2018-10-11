package com.sunquan.mediaeditor.photo.choose;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunquan.mediaeditor.R;
import com.sunquan.mediaeditor.component.ME;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/11
 **/
public class MediaDirAdapter extends RecyclerView.Adapter {
    private Context context;
    public final static String ALL_PHOTO = "全部照片";
    private String curDir = ALL_PHOTO;
    private List<String> items = new ArrayList<>();
    private OnDirItemClickListener listener;

    public MediaDirAdapter(Context context) {
        this.context = context;
        ME.media(context).setOnMediaDirChangeListener(dir -> {
            ((Activity) context).runOnUiThread(() -> {
                if (!items.contains(dir)) {
                    items.add(dir);
                    notifyItemInserted(items.size() - 1);
                }
            });
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MediaDirVH(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MediaDirVH) {
            ((MediaDirVH) holder).bind(items.get(position), position, (MediaDirVH) holder);
        }
    }

    public void setData(Set<String> dirs) {
        items.add(ALL_PHOTO);
        items.addAll(dirs);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class MediaDirVH extends RecyclerView.ViewHolder {
        TextView name;

        public MediaDirVH(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media_dir, parent, false));
            name = itemView.findViewById(R.id.name);
        }

        public void bind(String nameStr, int position, MediaDirVH holder) {
            name.setText(nameStr);
            if (curDir.equals(nameStr)) {
                name.setTextColor(Color.BLUE);
            } else {
                name.setTextColor(Color.BLACK);

            }
            name.setOnClickListener(v -> {
                curDir = nameStr;
                notifyDataSetChanged();
                if (listener != null) {
                    listener.click(nameStr);
                }
            });
        }
    }

    public void setOnDirItemClickListener(OnDirItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnDirItemClickListener {
        void click(String dir);
    }
}
