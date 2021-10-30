package com.queerlab.chat.widget.image;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.widget.image
 * @ClassName: ImageLoader
 * @Description: 图片加载公共框架
 * @Author: 鹿鸿祥
 * @CreateDate: 5/28/21 3:34 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/28/21 3:34 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ImageLoader {

    /**
     * 加载图片
     *
     * @param context 上下文
     * @param config 图片config
     */
    public static void load(Context context, ImageConfig config) {
        RequestOptions options = new RequestOptions()
                .placeholder(config.getPlaceholder())
                .error(config.getErrorPic());
        if (config.isCenterCrop()) {
            options = options.centerCrop();
        }
        if (config.isCircle()) {
            options = options.circleCrop();
        }
        if (config.getImageRadius() > 0) {
            options = options.transform(new RoundedCorners(config.getImageRadius()));
        }
        if (config.getBlurValue() > 0) {
            options = options.transform(new BlurTransformation(config.getBlurValue()));
        }
        RequestManager requestManager = Glide.with(context);
        RequestBuilder<Drawable> requestBuilder = requestManager.load(config.getUrl());
        if (config.isCrossFade()) {
            requestBuilder = requestBuilder.transition(DrawableTransitionOptions.withCrossFade());
        }
        requestBuilder.apply(options).into(config.getImageView());
    }
}
