package com.queerlab.chat.widget.image;

import android.graphics.Bitmap;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.widget.image
 * @ClassName: BlurTransformation
 * @Description: 高斯模糊
 * @Author: 鹿鸿祥
 * @CreateDate: 5/28/21 3:35 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/28/21 3:35 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class BlurTransformation extends BitmapTransformation {
    private static final String ID = BlurTransformation.class.getName();
    private static final byte[] ID_BYTES = ID.getBytes(Key.CHARSET);
    public static final int DEFAULT_RADIUS = 15;
    private int mRadius = DEFAULT_RADIUS;

    public BlurTransformation(@IntRange(from = 0) int radius) {
        mRadius = radius;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return FastBlur.doBlur(toTransform, mRadius, true);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BlurTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }
}
