package com.queerlab.chat.push;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.push
 * @ClassName: PrivateConstants
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/10/21 11:53 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/10/21 11:53 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PrivateConstants {
    // bugly上报
    public static final String BUGLY_APPID = "";

    /****** 华为离线推送参数start ******/
    // 在腾讯云控制台上传第三方推送证书后分配的证书ID
    public static final long HW_PUSH_BUZID = 19374;
    // 华为开发者联盟给应用分配的应用APPID
    public static final String HW_PUSH_APPID = "104659701"; // 见清单文件
    /****** 华为离线推送参数end ******/

    /****** 小米离线推送参数start ******/
    // 在腾讯云控制台上传第三方推送证书后分配的证书ID
    public static final long XM_PUSH_BUZID = 19316;
    // 小米开放平台分配的应用APPID及APPKEY
    public static final String XM_PUSH_APPID = "5901998449727";
    public static final String XM_PUSH_APPKEY = "QeoS9/O5OnMSOiGS8KLOlg==";
    /****** 小米离线推送参数end ******/

    /****** 魅族离线推送参数start ******/
    // 在腾讯云控制台上传第三方推送证书后分配的证书ID
    public static final long MZ_PUSH_BUZID = 0;
    // 魅族开放平台分配的应用APPID及APPKEY
    public static final String MZ_PUSH_APPID = "";
    public static final String MZ_PUSH_APPKEY = "";
    /****** 魅族离线推送参数end ******/

    /****** vivo离线推送参数start ******/
    // 在腾讯云控制台上传第三方推送证书后分配的证书ID
    public static final long VIVO_PUSH_BUZID = 19495;
    // vivo开放平台分配的应用APPID及APPKEY
    public static final String VIVO_PUSH_APPID = "105507162"; // 见清单文件
    public static final String VIVO_PUSH_APPKEY = "b3759a42e162e5b0300743883294b75f"; // 见清单文件
    /****** vivo离线推送参数end ******/

    /****** google离线推送参数start ******/
    // 在腾讯云控制台上传第三方推送证书后分配的证书ID
    public static final long GOOGLE_FCM_PUSH_BUZID = 0;
    /****** google离线推送参数end ******/

    /****** oppo离线推送参数start ******/
    // 在腾讯云控制台上传第三方推送证书后分配的证书ID
    public static final long OPPO_PUSH_BUZID = 19317;
    // oppo开放平台分配的应用APPID及APPKEY
    public static final String OPPO_PUSH_APPID = "30611705";
    public static final String OPPO_PUSH_APPKEY = "49b2641333cc49a28bc651219f2666c6";
    public static final String OPPO_PUSH_APPSECRET = "bd8fa8a68f424824bdd4f76d2f1f9c10";
    /****** oppo离线推送参数end ******/
}
