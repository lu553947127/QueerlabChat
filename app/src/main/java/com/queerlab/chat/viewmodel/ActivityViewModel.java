package com.queerlab.chat.viewmodel;

import androidx.lifecycle.MutableLiveData;
import com.queerlab.chat.bean.GroupTypeBean;
import com.queerlab.chat.http.retrofit.BaseRepository;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.viewmodel
 * @ClassName: ActivityViewModel
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/11/16 08:56
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/11/16 08:56
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ActivityViewModel extends BaseRepository {

    public MutableLiveData<String> pageStateLiveData;
    public MutableLiveData<String> failStateLiveData;
    public MutableLiveData<GroupTypeBean> activityTypeLiveData;
    private int page = 1;

    public ActivityViewModel(){
        pageStateLiveData = new MutableLiveData<>();
        failStateLiveData = new MutableLiveData<>();
        activityTypeLiveData = new MutableLiveData<>();
    }

    /**
     * 获取活动类型列表
     *
     */
    public void getActivityType(){
        request(apiService.getGroupType("2", 1, 100)).setData(activityTypeLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }
}
