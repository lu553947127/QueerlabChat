package com.queerlab.chat.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.queerlab.chat.bean.NoticeBean;
import com.queerlab.chat.http.retrofit.BaseRepository;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.viewmodel
 * @ClassName: NoticeViewModel
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 6/9/21 10:32 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/9/21 10:32 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class NoticeViewModel extends BaseRepository {
    public MutableLiveData<String> pageStateLiveData;
    public MutableLiveData<String> failStateLiveData;
    public MutableLiveData<NoticeBean> noticeLiveData;

    public NoticeViewModel(){
        pageStateLiveData = new MutableLiveData<>();
        failStateLiveData = new MutableLiveData<>();
        noticeLiveData = new MutableLiveData();
    }

    /**
     * 系统通知
     */
    public void notice() {
        request(apiService.notice()).setData(noticeLiveData).setPageState(pageStateLiveData).send();
    }
}
