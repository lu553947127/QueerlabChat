package com.queerlab.chat.utils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import androidx.appcompat.widget.AppCompatImageView;

import com.blankj.utilcode.util.LogUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.widget.image.ImageConfig;
import com.queerlab.chat.widget.image.ImageLoader;
import com.queerlab.chat.widget.image.MatisseGlideEngine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Set;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: PictureUtils
 * @Description: 图片相关的工具类
 * @Author: 鹿鸿祥
 * @CreateDate: 5/28/21 3:37 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/28/21 3:37 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PictureUtils {

    /*相册选择后图片 请求码code*/
    public static final int ALBUM_CODE = 1999;

    /**
     * 设置图片显示
     *
     * @param context
     * @param image
     * @param appCompatImageView
     */
    public static void setImage(Context context, String image, AppCompatImageView appCompatImageView){
        ImageLoader.load(context, new ImageConfig.Builder()
                .url(image)
                .placeholder(R.drawable.svg_icon_empty_user)
                .errorPic(R.drawable.svg_icon_empty_user)
                .imageView(appCompatImageView)
                .build());
    }

    /**
     * 打开相册
     *
     * @param activity 上下文
     * @param mimeTypes 文件选择类型
     * @param capture 是否打开相机
     * @param maxSelectable 最大选择图片数
     */
    public static void openAlbum(BaseActivity activity, Set<MimeType> mimeTypes, boolean capture, int maxSelectable) {
        Matisse
                //上下文
                .from(activity)
                /**
                 * 文件选择类型
                 * MimeType.ofAll() 选择全部
                 * MimeType.ofImage() 选择图片
                 * MimeType.ofVideo() 选择视频
                 * MimeType.of(MimeType.JPEG,MimeType.AVI) 自定义选择选择的类型
                 */

                .choose(mimeTypes)
                //是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
                .showSingleMediaType(true)
                //这两行要连用 是否在选择图片中展示照相
                .capture(capture)
                //适配安卓7.0 FileProvider
                .captureStrategy(new CaptureStrategy(true, "com.queerlab.chat.FileProvider"))
                //true：选中后显示数字  false：选中后显示对号
                .countable(true)
                //最大选择数量
                .maxSelectable(maxSelectable)
                //选择方向
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                //界面中缩略图的质量
                .thumbnailScale(0.85f)
                /**
                 * 设置显示主题
                 * R.style.Matisse_Dracula 这是自定义主题
                 * R.style.Matisse_Zhihu 蓝色主题
                 * R.style.Matisse_Dracula 黑色主题
                 */
                .theme(R.style.Matisse_Dracula)
                //Glide加载方式
                .imageEngine(new MatisseGlideEngine())
                //请求码
                .forResult(ALBUM_CODE);
    }

    /**
     * Uri转换成path路径
     *
     * @param context 上下文
     * @param uri android10后本地返回的图片uri
     * @return
     */
    public static String getUriRealFilePath(Context context, Uri uri) {
        try {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            int nameIndex = Objects.requireNonNull(returnCursor).getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String name = (returnCursor.getString(nameIndex));
            File file = new File(context.getFilesDir(), name);
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            returnCursor.close();
            inputStream.close();
            outputStream.close();
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 裁剪uri转化成功file地址
     *
     * @param uri
     * @return
     */
    public static String getUriFile(Uri uri){
        String path = null;
        try {
            File file = new File(new URI(uri.toString()));
            path = file.getPath();
            LogUtils.e(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return path;
    }
}
