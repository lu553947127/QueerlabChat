package com.queerlab.chat.widget.image;

import android.widget.ImageView;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.widget.image
 * @ClassName: ImageConfig
 * @Description: 图片展示实体解析类
 * @Author: 鹿鸿祥
 * @CreateDate: 5/28/21 3:34 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/28/21 3:34 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ImageConfig {
    private String url;
    private ImageView imageView;
    private int placeholder;//占位符
    private int errorPic;//错误占位符
    private boolean isCrossFade;//是否使用淡入淡出过渡动画
    private boolean isCenterCrop;//是否将图片剪切为 CenterCrop
    private boolean isCircle;//是否将图片剪切为圆形
    private int imageRadius;//图片每个圆角的大小
    private int blurValue;//高斯模糊值, 值越大模糊效果越大

    private ImageConfig(Builder builder) {
        this.url = builder.url;
        this.imageView = builder.imageView;
        this.placeholder = builder.placeholder;
        this.errorPic = builder.errorPic;
        this.isCrossFade = builder.isCrossFade;
        this.isCenterCrop = builder.isCenterCrop;
        this.isCircle = builder.isCircle;
        this.imageRadius = builder.imageRadius;
        this.blurValue = builder.blurValue;
    }

    public String getUrl() {
        return url;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public int getErrorPic() {
        return errorPic;
    }

    public boolean isCrossFade() {
        return isCrossFade;
    }

    public boolean isCenterCrop() {
        return isCenterCrop;
    }

    public boolean isCircle() {
        return isCircle;
    }

    public int getImageRadius() {
        return imageRadius;
    }

    public int getBlurValue() {
        return blurValue;
    }

    public static final class Builder{

        private String url;
        private ImageView imageView;
        private int placeholder;//占位符
        private int errorPic;//错误占位符
        private boolean isCrossFade;//是否使用淡入淡出过渡动画
        private boolean isCenterCrop;//是否将图片剪切为 CenterCrop
        private boolean isCircle;//是否将图片剪切为圆形
        private int imageRadius;//图片每个圆角的大小
        private int blurValue;//高斯模糊值, 值越大模糊效果越大

        public Builder() {
        }

        public Builder url(String url){
            this.url = url;
            return this;
        }

        public Builder imageView(ImageView imageView){
            this.imageView = imageView;
            return this;
        }

        public Builder placeholder(int placeholder){
            this.placeholder = placeholder;
            return this;
        }

        public Builder errorPic(int errorPic){
            this.errorPic = errorPic;
            return this;
        }

        public Builder isCrossFade(boolean isCrossFade){
            this.isCrossFade = isCrossFade;
            return this;
        }

        public Builder isCenterCrop(boolean isCenterCrop){
            this.isCenterCrop = isCenterCrop;
            return this;
        }

        public Builder isCircle(boolean isCircle){
            this.isCircle = isCircle;
            return this;
        }

        public Builder imageRadius(int imageRadius){
            this.imageRadius = imageRadius;
            return this;
        }

        public Builder blurValue(int blurValue){
            this.blurValue = blurValue;
            return this;
        }

        public ImageConfig build(){
            return new ImageConfig(this);
        }

    }
}
