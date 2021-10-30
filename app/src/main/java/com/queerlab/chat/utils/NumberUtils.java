package com.queerlab.chat.utils;

import com.queerlab.chat.base.BaseActivity;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: NumberUtils
 * @Description: 数字工具类
 * @Author: 鹿鸿祥
 * @CreateDate: 5/8/21 2:00 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/8/21 2:00 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class NumberUtils {

    /**
     * 根据区号判断是否是正确的电话号码
     *
     * @param activity 上下文
     * @param phoneNumber 带国家码的电话号码
     * @param countryCode 默认国家码
     * @return true 合法  false：不合法
     */
    public static boolean isPhoneNumberValid(BaseActivity activity, String phoneNumber, String countryCode){
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(activity);
        try{
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, countryCode);
//            LogUtils.e("isPhoneNumberValid: " + phoneUtil.isValidNumber(numberProto));
            return phoneUtil.isValidNumber(numberProto);
        }catch (NumberParseException e){
//            LogUtils.e("isPhoneNumberValid NumberParseException was thrown: " + e.toString());
        }
        return false;
    }
}
