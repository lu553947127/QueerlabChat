package com.queerlab.chat.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.queerlab.chat.base.PageState;
import com.queerlab.chat.bean.GroupEmoBean;
import com.queerlab.chat.bean.GroupTypeBean;
import com.queerlab.chat.http.retrofit.BaseRepository;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.viewmodel
 * @ClassName: NewGroupViewModel
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 2021/11/15 15:47
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/11/15 15:47
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class NewGroupViewModel extends BaseRepository {
    public MutableLiveData<String> pageStateLiveData;
    public MutableLiveData<String> failStateLiveData;
    public MutableLiveData<GroupTypeBean> groupTypeLiveData;
    public MutableLiveData<GroupEmoBean> groupEmoLiveData;
    private int page = 1;
    private int pageLive = 1;

    public NewGroupViewModel(){
        groupTypeLiveData = new MutableLiveData<>();
        groupEmoLiveData = new MutableLiveData<>();
    }

    /**
     * 获取小组类型列表
     *
     */
    public void getGroupType(){
        request(apiService.getGroupType("1", 1, 100)).setData(groupTypeLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 获取活动emo列表
     */
    public void getGroupEmo(){
        page = 1;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.getGroupEmo(page, 104)).setData(groupEmoLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 获取活动emo列表
     */
    public void getGroupEmoMore(){
        page ++;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.getGroupEmo(page, 104)).setData(groupEmoLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }
}
