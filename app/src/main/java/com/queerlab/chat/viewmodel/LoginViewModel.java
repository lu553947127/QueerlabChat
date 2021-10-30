package com.queerlab.chat.viewmodel;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.queerlab.chat.bean.LoginBean;
import com.queerlab.chat.http.retrofit.BaseRepository;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.viewmodel
 * @ClassName: LoginViewModel
 * @Description: 登录ViewModel
 * @Author: 鹿鸿祥
 * @CreateDate: 5/21/21 11:46 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/21/21 11:46 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@SuppressLint("CheckResult")
public class LoginViewModel extends BaseRepository {
    public MutableLiveData<String> pageStateLiveData;
    public MutableLiveData<String> failStateLiveData;
    public MutableLiveData sendSmsLiveData;
    public MutableLiveData<Long> timeLiveDataLiveData;
    public MutableLiveData<LoginBean> loginLiveData;
    public MutableLiveData<LoginBean> userAddLiveData;

    public LoginViewModel() {
        pageStateLiveData = new MutableLiveData<>();
        failStateLiveData = new MutableLiveData<>();
        sendSmsLiveData = new MutableLiveData();
        timeLiveDataLiveData = new MutableLiveData<>();
        loginLiveData = new MutableLiveData<>();
        userAddLiveData = new MutableLiveData();
    }

    /**
     * 获取验证码
     *
     * @param countryCode 国家编码
     * @param phoneNumber 手机号
     */
    public void sendSms(String countryCode, String phoneNumber) {
        request(apiService.sendSms(countryCode, phoneNumber)).setData(sendSmsLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 获取验证码倒计时
     */
    public void sendVerificationCode() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(60)
                .map(aLong -> 59 - aLong)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> timeLiveDataLiveData.postValue(aLong), throwable -> {

                }, () -> {
                    //倒计时结束，重置按钮，并停止获取请求
                    timeLiveDataLiveData.postValue((long) -1);
                });
    }

    /**
     * 验证码登录
     *
     * @param countryCode 国家编码
     * @param phoneNumber 手机号
     * @param code 验证码
     */
    public void login(String countryCode, String phoneNumber, String code) {
        request(apiService.login(countryCode, phoneNumber, code)).setData(loginLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 用户信息添加
     *
     * @param userName 用户姓名
     * @param countryCode 国家号
     * @param phoneNumber 手机号
     * @param userType 兴趣分类
     */
    public void userAdd(String userName, String countryCode, String phoneNumber, String userType) {
        request(apiService.userAdd(userName, countryCode, phoneNumber, userType)).setData(userAddLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }
}
