package com.queerlab.chat.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.queerlab.chat.R;
import com.queerlab.chat.base.BaseActivity;
import com.queerlab.chat.event.PermissionEvent;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.zhihu.matisse.MimeType;

import org.greenrobot.eventbus.EventBus;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: PermissionUtils
 * @Description: 权限设置开启工具类
 * @Author: 鹿鸿祥
 * @CreateDate: 5/20/21 11:03 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/20/21 11:03 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@SuppressLint("ObsoleteSdkInt")
public class PermissionUtils {

    /**
     * 检测定位权限
     *
     * @param activity 上下文
     */
    public static boolean isCheckPermission(BaseActivity activity){
        return AndPermission.hasPermissions(activity, Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION);
    }

    /**
     * 开启定位权限
     *
     * @param activity 上下文
     * @param tencentMap 腾讯地图
     */
    public static void getOpenLocationPermission(BaseActivity activity, TencentMap tencentMap){
        AndPermission.with(activity)
                .runtime()
                .permission(Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION)
                .onGranted(permissions -> {
                    LocationUtils.getInstance().startLocalService(tencentMap);
                })
                .onDenied(permissions ->{
                    activity.showPermissionDialog(activity.getResources().getString(R.string.permission_location));
                })
                .start();
    }

    /**
     * 开启本地存储权限
     *
     * @param activity 上下文
     */
    public static void openStoragePermission(BaseActivity activity, int maxSelectable){
        AndPermission.with(activity)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .onGranted(permissions -> {
                    PictureUtils.openAlbum(activity, MimeType.ofImage(), false, maxSelectable);
                })
                .onDenied(permissions ->{
                    activity.showPermissionDialog(activity.getResources().getString(R.string.permission_storage));
                })
                .start();
    }

    /**
     * 开启麦克风权限
     *
     * @param activity
     */
    public static void openStorageAudio(BaseActivity activity){
        AndPermission.with(activity)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.RECORD_AUDIO)
                .onGranted(permissions -> {
                    EventBus.getDefault().post(new PermissionEvent());
                })
                .onDenied(permissions ->{
                    activity.showPermissionDialog(activity.getResources().getString(R.string.permission_audio));
                })
                .start();
    }

    /**
     * 跳转权限设置页
     *
     * @param context
     */
    public static void toSelfSetting(Context context) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(mIntent);
    }
}
