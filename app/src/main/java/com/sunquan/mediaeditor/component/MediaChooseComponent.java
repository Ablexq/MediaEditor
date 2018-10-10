package com.sunquan.mediaeditor.component;

/**
 * @author sunquan
 * sunquan@bitstarlight.com
 * @date 2018/10/9
 **/
public final class MediaChooseComponent {
    private volatile static MediaChooseComponent mediaChooseComponent = null;

    private MediaChooseComponent() {

    }

    public static MediaChooseComponent getIns() {
        if (mediaChooseComponent == null) {
            synchronized (MediaChooseComponent.class) {
                if (mediaChooseComponent == null) {
                    mediaChooseComponent = new MediaChooseComponent();
                }
            }
        }
        return mediaChooseComponent;
    }
}
