package com.queerlab.chat.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: AutoUtils
 * @Description: 今日头条提供的屏幕适配方案
 * @Author: 鹿鸿祥
 * @CreateDate: 5/6/21 1:47 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/6/21 1:47 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class AutoUtils {
    private static float sNoCompatDensity;
    private static float sNoCompatScaleDensity;
    private static final float width = 375;

    public static void setCustomDensity(@NonNull Activity activity, @NonNull final Application application){
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if (sNoCompatDensity == 0){
            sNoCompatDensity = appDisplayMetrics.density;
            sNoCompatScaleDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0){
                        sNoCompatScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        final float targetDensity = appDisplayMetrics.widthPixels / width;
        final float targetScaleDensity = targetDensity * (sNoCompatScaleDensity / sNoCompatDensity);
        final int targetDensityDpi = (int) (targetDensity * 160);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaleDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaleDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }
}
