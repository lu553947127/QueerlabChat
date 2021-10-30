package com.queerlab.chat.utils;

import android.net.Uri;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import kotlin.jvm.internal.Intrinsics;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: CropUtils
 * @Description: 裁剪图片工具类
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/8/18 09:50
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/8/18 09:50
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class CropUtils {

    /**
     * 使用UCrop开始裁剪图片
     *
     * @param activity 上下文
     * @param uriSource 图片源
     */
    public static void cropUriPicture(BaseActivity activity, Uri uriSource) {
        Intrinsics.checkParameterIsNotNull(uriSource, "资源为空");
        //保存裁剪后的图片uri
        Uri uriResult = getDestinationUri(activity);
        UCrop uCrop = UCrop.of(uriSource, uriResult);
        //宽高比
        uCrop.withAspectRatio(1f, 1f);
        //宽高
        uCrop.withMaxResultSize(500, 2960);
        //手动设置高级选项
        UCrop.Options options = new UCrop.Options();
        //设置裁剪主题色 裁剪框颜色
        options.setCropFrameColor(ContextCompat.getColor(activity, R.color.color_FFA52C));
        //设置裁剪网格框颜色
        options.setCropGridColor(ContextCompat.getColor(activity, R.color.color_FFA52C));
        //设置裁剪网格角颜色
        options.setCropGridCornerColor(ContextCompat.getColor(activity, R.color.color_FFA52C));
        //设置页面状态栏颜色
        options.setStatusBarColor(ContextCompat.getColor(activity, R.color.color_FFA52C));
        //设置头部背景色
        options.setToolbarColor(ContextCompat.getColor(activity, R.color.color_FFA52C));
        //设置头部控件的颜色
        options.setToolbarWidgetColor(ContextCompat.getColor(activity, R.color.white));
        //设置缩放滚动条/底部icon图标颜色
        options.setActiveControlsWidgetColor(ContextCompat.getColor(activity, R.color.color_FFA52C));
        //生效配置信息
        uCrop = uCrop.withOptions(options);
        //跳转裁剪页面
        uCrop.start(activity);
    }

    /**
     * 创建裁剪图片保存结果的Uri
     *
     * @param activity
     * @return
     */
    private static Uri getDestinationUri(BaseActivity activity) {
        String fileName = String.format("wonderland_%s.jpg", System.currentTimeMillis());
        File cropFile = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
        return Uri.fromFile(cropFile);
    }
}
