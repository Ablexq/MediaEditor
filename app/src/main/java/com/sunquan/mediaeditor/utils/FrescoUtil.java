package com.sunquan.mediaeditor.utils;

import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

public class FrescoUtil {

    public static void loadImageLocalUrl(SimpleDraweeView simpleDraweeView, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Uri uri = Uri.fromFile(new File(url));
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(simpleDraweeView.getController())
                .setImageRequest(request)
                //设置图片自动播放属性
                .setAutoPlayAnimations(true)
                .build();
        simpleDraweeView.setController(controller);
    }
}
