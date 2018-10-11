package com.sunquan.mediaeditor.component;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.sunquan.mediaeditor.model.MediaItem;
import com.sunquan.mediaeditor.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/9
 **/
public final class MediaChooseComponent {
    private volatile static MediaChooseComponent mediaChooseComponent = null;
    private final ContentResolver mediaResolver;
    private OnMediaChangeListener listener;
    private OnMediaDirChangeListener dirListener;
    private Thread imageQueryThread;
    private List<MediaItem> imageMediasCache = new ArrayList<>();
    private List<MediaItem> videoMediasCache = new ArrayList<>();

    private Set<String> imageDirs = new HashSet<>();
    private HashMap<String, List<MediaItem>> dirMapImage = new HashMap<>();

    private List<String> videoDirs = new ArrayList<>();
    private HashMap<String, List<MediaItem>> dirMapVideos = new HashMap<>();

    public void setOnMediaChangeListener(OnMediaChangeListener listener) {
        this.listener = listener;
    }

    public void setOnMediaDirChangeListener(OnMediaDirChangeListener dirListener) {
        this.dirListener = dirListener;
    }

    private MediaChooseComponent(Context context) {
        this.mediaResolver = context.getContentResolver();
    }

    public void loadImageMedias(String dir) {
        if (imageQueryThread != null && imageQueryThread.isAlive()) {
            imageQueryThread.interrupt();
            imageQueryThread = null;
        }
        ArrayList<MediaItem> medias = new ArrayList<>();
        if (TextUtils.isEmpty(dir)) {
            medias.addAll(imageMediasCache);
        } else {
            if (dirMapImage.get(dir) != null) {
                medias.addAll(dirMapImage.get(dir));
            }
        }

        for (MediaItem mediaInfo : medias) {
            if (listener != null) {
                listener.notifyMedia(mediaInfo);
            }
        }
        imageQueryThread = new Thread(() -> {
            queryImageList(dir);
        });
        imageQueryThread.start();
    }

    private void queryImageList(String dir) {
        Cursor imageCursor = null;
        try {
            imageCursor = mediaResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.TITLE,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.Media.DATE_ADDED,
            }, String.format("%1$s != ?", MediaStore.Images.Media.MIME_TYPE), new String[]{
                    "image/gif"
            }, MediaStore.Images.Media.DATE_ADDED + " DESC");
            if (imageCursor == null) {
                return;
            }

            int col_mine_type_image = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);
            int col_data_image = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int col_title_image = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            int col_id_image = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int col_date_added_image = imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
            while (imageCursor.moveToNext()) {
                String filePath = imageCursor.getString(col_data_image);
                if (TextUtils.isEmpty(filePath)) {
                    continue;
                }
                final String mediaDir = getMediaDir(filePath);
                imageDirs.add(mediaDir);
                if (dirListener != null) {
                    dirListener.notifyMediaDir(mediaDir);
                }
                if (!TextUtils.isEmpty(dir) && !isMatch(getMediaDir(filePath), dir)) {
                    continue;
                }

                MediaItem imageInfo = newMediaItem(imageCursor, col_mine_type_image, col_data_image, col_title_image, col_id_image, col_date_added_image);
                if (imageInfo == null || isExistInImageCache(imageInfo, imageMediasCache)) {
                    continue;
                }

                imageMediasCache.add(imageInfo);
                if (dirMapImage.get(imageInfo.dir) == null) {
                    List<MediaItem> medias = new ArrayList<>();
                    medias.add(imageInfo);
                    dirMapImage.put(imageInfo.dir, medias);
                } else {
                    final List<MediaItem> mediaInfos = dirMapImage.get(imageInfo.dir);
                    if (!isExistInImageCache(imageInfo, mediaInfos)) {
                        mediaInfos.add(imageInfo);
                    }
                }
                if (listener != null && isMatch(imageInfo.dir, dir)) {
                    listener.notifyMedia(imageInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageCursor != null) {
                try {
                    imageCursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isMatch(String mediaDir, String dir) {
        if (TextUtils.isEmpty(dir)) {
            return true;
        }
        return mediaDir.equals(dir);
    }

    private MediaItem newMediaItem(Cursor cursor, int col_mine_type, int col_data, int col_title, int col_id, int col_date_added) {

        String mimeType = cursor.getString(col_mine_type);
        String filePath = cursor.getString(col_data);
        if (!new File(filePath).exists()) {
            return null;
        }
        MediaItem mediaInfo = new MediaItem();
        mediaInfo.type = Constants.MediaType.MEDIA_PHOTO;
        String title = cursor.getString(col_title);
        mediaInfo.filePath = filePath;
        mediaInfo.mimeType = mimeType;
        mediaInfo.title = title;
        mediaInfo.id = cursor.getInt(col_id);
        mediaInfo.dir = getMediaDir(filePath);
        Cursor thumbCursor = mediaResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Thumbnails.DATA,
                        MediaStore.Images.Thumbnails.IMAGE_ID
                },
                MediaStore.Images.Thumbnails.IMAGE_ID + "=?",
                new String[]{String.valueOf(mediaInfo.id)}, null);
        if (thumbCursor.getCount() == 0) {
            thumbCursor.close();
            thumbCursor = createThumbnailAndRequery(mediaInfo);
        }
        if (thumbCursor.moveToFirst()) {
            String thumbPath = thumbCursor.getString(
                    thumbCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
            mediaInfo.thumbnailPath = thumbPath;
            checkIfNeedToRotateThumbnail(mediaInfo.filePath, thumbPath);
        }
        thumbCursor.close();

        return mediaInfo;
    }

    private String getMediaDir(String filePath) {
        String[] dir = filePath.split("/");
        return dir[dir.length - 2];
    }

    private void checkIfNeedToRotateThumbnail(String filePath, String thumbnailPath) {
        try {
            ExifInterface fileEi = new ExifInterface(filePath);
            ExifInterface thumbnailEi = new ExifInterface(thumbnailPath);
            int orientationFile = fileEi.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int orientationThumbnail = thumbnailEi.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (orientationFile != orientationThumbnail) {
                thumbnailEi.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(orientationFile));
                thumbnailEi.saveAttributes();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Cursor createThumbnailAndRequery(MediaItem info) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        MediaStore.Images.Thumbnails.getThumbnail(mediaResolver,
                info.id, MediaStore.Images.Thumbnails.MICRO_KIND, options);
        Cursor thumbCursor = mediaResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Thumbnails.DATA,
                        MediaStore.Images.Thumbnails.IMAGE_ID
                },
                MediaStore.Images.Thumbnails.IMAGE_ID + "=?",
                new String[]{String.valueOf(info.id)}, null);
        return thumbCursor;
    }

    public boolean isExistInImageCache(MediaItem mediaInfo, List<MediaItem> mediaInfos) {
        for (int i = 0; i < mediaInfos.size(); i++) {
            if (!TextUtils.isEmpty(mediaInfo.filePath)
                    && !TextUtils.isEmpty(mediaInfos.get(i).filePath)
                    && mediaInfo.filePath.equals(mediaInfos.get(i).filePath)) {
                return true;
            }
        }
        return false;
    }

    public static MediaChooseComponent getIns(Context context) {
        if (mediaChooseComponent == null) {
            synchronized (MediaChooseComponent.class) {
                if (mediaChooseComponent == null) {
                    mediaChooseComponent = new MediaChooseComponent(context);
                }
            }
        }
        return mediaChooseComponent;
    }

    public Set<String> getDirs() {
        return imageDirs;
    }

    public interface OnMediaDirChangeListener {
        void notifyMediaDir(String dir);
    }

    public interface OnMediaChangeListener {
        void notifyMedia(MediaItem mediaInfo);
    }
}
