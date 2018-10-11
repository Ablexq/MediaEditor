package com.sunquan.mediaeditor.model;

import java.io.Serializable;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/10
 **/
public class MediaInfo implements Serializable {
    public static final int TYPE_VIDEO = 0;
    public static final int TYPE_PHOTO = 1;
    public String filePath;
    public String outputPath;
    public String thumbnailPath;
    public String mimeType;
    public String title;
    public long duration;
    public int id;
    public int type;
    public int mediaHeight;
    public int mediaWidth;
    public String dir;

    public MediaInfo(String filePath, String mimeType, long duration, int type, int mediaHeight, int mediaWidth) {
        this.filePath = filePath;
        this.mimeType = mimeType;
        this.duration = duration;
        this.type = type;
        this.mediaHeight = mediaHeight;
        this.mediaWidth = mediaWidth;
    }

    public MediaInfo() {

    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MediaInfo) {
            MediaInfo info = (MediaInfo) o;
            return id == info.id;
        }
        return false;
    }
}
