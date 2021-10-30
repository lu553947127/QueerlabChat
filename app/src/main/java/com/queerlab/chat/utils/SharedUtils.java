package com.queerlab.chat.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: SharedUtils
 * @Description: 自定义存储工具（此工具存储后退出登录，本地数据不清除缓存，若想进行存储后清除缓存，请用SPUtils工具类）
 * @Author: 鹿鸿祥
 * @CreateDate: 5/7/21 10:41 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/7/21 10:41 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SharedUtils {
    private final Context context;//上下文，获取去shared

    public SharedUtils(Context context){
        this.context = context;
    }

    /**
     * 本地存储方法
     *
     * @param key 存储名称标识
     * @param value 存储数据对象
     * @param name 存储表名称
     */
    public void addShared(String key, String value , String name){
        //SharedPreferences的获取
        SharedPreferences shared = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        //editor->添加并提交内容以及clear
        SharedPreferences.Editor editor = shared.edit();
        //添加要保存内容
        editor.putString(key, value);
        //提交要保存内容
        editor.commit();
    }

    /**
     * 本地获取方法
     *
     * @param key 存储名称标识
     * @param name 存储表名称
     * @return
     */
    public String getShared(String key, String name){
        //SharedPreferences的获取
        SharedPreferences shared = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        //按照传入key获取value如果key不存在返回参数二添加的默认内容
        String result = shared.getString(key, "");
        return result;
    }

    /**
     * 本地清空方法
     *
     * @param name 存储表名称
     */
    public void clearShared(String name){
        SharedPreferences shared = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.clear();
        editor.commit();
    }
}
