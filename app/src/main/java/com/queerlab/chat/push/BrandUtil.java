package com.queerlab.chat.push;

import android.os.Build;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.push
 * @ClassName: BrandUtil
 * @Description: 判断android设备工具
 * @Author: 鹿鸿祥
 * @CreateDate: 6/10/21 11:53 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/10/21 11:53 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class BrandUtil {
    /**
     * 判断是否为小米设备
     */
    public static boolean isBrandXiaoMi() {
        return "xiaomi".equalsIgnoreCase(Build.BRAND)
                || "xiaomi".equalsIgnoreCase(Build.MANUFACTURER);
    }

    /**
     * 判断是否为华为设备
     */
    public static boolean isBrandHuawei() {
        return "huawei".equalsIgnoreCase(Build.BRAND)
                || "huawei".equalsIgnoreCase(Build.MANUFACTURER);
    }

    /**
     * 判断是否为魅族设备
     */
    public static boolean isBrandMeizu() {
        return "meizu".equalsIgnoreCase(Build.BRAND)
                || "meizu".equalsIgnoreCase(Build.MANUFACTURER)
                || "22c4185e".equalsIgnoreCase(Build.BRAND);
    }

    /**
     * 判断是否是oppo设备
     *
     * @return
     */
    public static boolean isBrandOppo() {
        return "oppo".equalsIgnoreCase(Build.BRAND)
                || "oppo".equalsIgnoreCase(Build.MANUFACTURER);
    }

    /**
     * 判断是否是vivo设备
     *
     * @return
     */
    public static boolean isBrandVivo() {
        return "vivo".equalsIgnoreCase(Build.BRAND)
                || "vivo".equalsIgnoreCase(Build.MANUFACTURER);
    }
}
