package com.queerlab.chat.viewmodel;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.queerlab.chat.app.MyApplication;
import com.queerlab.chat.base.PageState;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.bean.LocationUserBean;
import com.queerlab.chat.bean.UploadFileBean;
import com.queerlab.chat.bean.UserInfoBean;
import com.queerlab.chat.http.retrofit.BaseRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.viewmodel
 * @ClassName: UserViewModel
 * @Description: 用户ViewModel
 * @Author: 鹿鸿祥
 * @CreateDate: 5/31/21 9:01 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/31/21 9:01 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@SuppressLint("CheckResult")
public class UserViewModel extends BaseRepository {

    public MutableLiveData<String> pageStateLiveData;
    public MutableLiveData<String> failStateLiveData;
    public MutableLiveData<UserInfoBean> userInfoLiveData;
    public MutableLiveData<UploadFileBean> uploadFileLiveData;
    public MutableLiveData updateStatusLiveData;
    public MutableLiveData updateNameLiveData;
    public MutableLiveData updateTypeLiveData;
    public MutableLiveData updateHideGroupLiveData;
    public MutableLiveData<LocationUserBean> userSearchLiveData;
    public MutableLiveData updateUserClapLiveData;
    private final String userId;
    private int page = 1;
    public String userType;//兴趣爱好

    public UserViewModel(){
        userId = SPUtils.getInstance().getString(SpConfig.USER_ID);
        pageStateLiveData = new MutableLiveData<>();
        failStateLiveData = new MutableLiveData<>();
        userInfoLiveData = new MutableLiveData();
        uploadFileLiveData = new MutableLiveData();
        updateStatusLiveData = new MutableLiveData();
        updateNameLiveData = new MutableLiveData();
        updateTypeLiveData = new MutableLiveData();
        updateHideGroupLiveData = new MutableLiveData();
        userSearchLiveData = new MutableLiveData();
        updateUserClapLiveData = new MutableLiveData();
    }

    /**
     * 获取用户详情
     *
     * @param userId
     */
    public void userInfo(String userId) {
        request(apiService.userInfo(userId)).setData(userInfoLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 压缩图片
     *
     * @param path
     */
    public void uploadPhoto(String path) {
        List<File> list = new ArrayList<>();
        list.add(new File(path));
        LogUtils.e(new File(path));
        //如果uri为空，代表为网络图片，无需压缩，直接增加即可
        Flowable.just(list)
                .observeOn(Schedulers.io())
                .doOnSubscribe(subscription -> pageStateLiveData.postValue(PageState.PAGE_LOADING))
                .map(list1 -> Luban.with(MyApplication.getInstance())
                        .ignoreBy(1024 * 2)
                        .setTargetDir(getPath())
                        .load(list1)
                        .get())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    ToastUtils.showShort(throwable.getMessage());
                    LogUtils.i(throwable.getMessage());
                })
                .onErrorResumeNext(Flowable.<List<File>>empty())
                .subscribe(files -> uploadFile(files.get(0)), throwable -> ToastUtils.showShort("图片过大"));
    }

    /**
     * 设置压缩后的路径
     *
     * @return
     */
    private String getPath() {
        String path = MyApplication.getInstance().getFilesDir() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    /**
     * 上传附近
     *
     * @param file
     */
    public void uploadFile(File file) {
        // 创建 RequestBody，用于封装 请求RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("userPortrait", file.getName(), requestFile);
        request(apiService.uploadFile(userId, body)).setData(uploadFileLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 更新状态
     *
     * @param userStatus
     */
    public void updateStatus(String userStatus) {
        request(apiService.updateStatus(userId, userStatus)).setData(updateStatusLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 修改昵称
     *
     * @param userName
     */
    public void updateName(String userName) {
        request(apiService.updateName(userId, userName)).setData(updateNameLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 编辑兴趣
     *
     */
    public void updateType() {
        request(apiService.updateType(userId, userType)).setData(updateTypeLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 隐藏去过的小组
     *
     * @param isHideGroup
     */
    public void updateHideGroup(String isHideGroup) {
        request(apiService.updateHideGroup(userId, isHideGroup)).setData(updateHideGroupLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 用户搜索列表
     */
    public void userSearch(String userName){
        page = 1;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.userSearch(userName, page ,10)).setData(userSearchLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 用户搜索列表
     */
    public void userSearchMore(String userName){
        page ++;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.userSearch(userName, page ,10)).setData(userSearchLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 用户拍一拍
     *
     * @param userId
     */
    public void updateUserClap(String userId) {
        request(apiService.updateUserClap(userId)).setData(updateUserClapLiveData).setPageState(pageStateLiveData).send();
    }
}
