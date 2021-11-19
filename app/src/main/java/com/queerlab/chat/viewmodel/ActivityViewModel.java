package com.queerlab.chat.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.SPUtils;
import com.queerlab.chat.base.PageState;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.bean.ActivityBannerBean;
import com.queerlab.chat.bean.ActivityDetailBean;
import com.queerlab.chat.bean.ActivityListBean;
import com.queerlab.chat.bean.GroupTypeBean;
import com.queerlab.chat.bean.MarkerActivityBean;
import com.queerlab.chat.http.retrofit.BaseRepository;

import java.util.List;

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
    public MutableLiveData<ActivityListBean> locationActivityLiveData;
    public MutableLiveData<ActivityListBean> searchActivityLiveData;
    public MutableLiveData<ActivityListBean> userJoinActivityLiveData;
    public MutableLiveData<ActivityListBean> activityListLiveData;
    public MutableLiveData<List<MarkerActivityBean>> markerActivityLiveData;
    public MutableLiveData<ActivityDetailBean> activityDetailLiveData;
    public MutableLiveData<List<ActivityBannerBean>> activityBannerLiveData;
    private final String userId;
    private int page = 1;

    public ActivityViewModel(){
        userId = SPUtils.getInstance().getString(SpConfig.USER_ID);
        pageStateLiveData = new MutableLiveData<>();
        failStateLiveData = new MutableLiveData<>();
        activityTypeLiveData = new MutableLiveData<>();
        locationActivityLiveData = new MutableLiveData<>();
        searchActivityLiveData = new MutableLiveData<>();
        userJoinActivityLiveData = new MutableLiveData<>();
        activityListLiveData = new MutableLiveData<>();
        markerActivityLiveData = new MutableLiveData<>();
        activityDetailLiveData = new MutableLiveData<>();
        activityBannerLiveData = new MutableLiveData<>();
    }

    /**
     * 获取活动类型列表
     *
     */
    public void getActivityType(){
        request(apiService.getGroupType("2", page, 100)).setData(activityTypeLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 获取马克点活动列表
     *
     * @param longitude
     * @param latitude
     */
    public void markerActivity(String longitude, String latitude){
        request(apiService.markerActivity(longitude, latitude)).setDataList(markerActivityLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 活动轮播列表
     *
     */
    public void activityBanner(){
        request(apiService.activityBanner()).setDataList(activityBannerLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 活动详情
     *
     * @param id
     */
    public void activityDetail(String id){
        request(apiService.activityDetail(id)).setData(activityDetailLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 根据经纬度活动列表
     *
     * @param longitude
     * @param latitude
     */
    public void locationActivity(String longitude, String latitude){
        page = 1;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.locationActivity(longitude, latitude, "", page, 10)).setData(locationActivityLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 根据经纬度活动列表
     *
     * @param longitude
     * @param latitude
     */
    public void locationActivityMore(String longitude, String latitude){
        page ++;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.locationActivity(longitude, latitude,"", page, 10)).setData(locationActivityLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 活动列表根据关键字搜索
     *
     * @param title
     */
    public void searchActivity(String title){
        page = 1;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.SearchActivity(title, page, 10)).setData(searchActivityLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 活动列表根据关键字搜索
     *
     * @param title
     */
    public void searchActivityMore(String title){
        page ++;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.SearchActivity(title, page, 10)).setData(searchActivityLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 用户参加的活动
     *
     */
    public void userJoinActivity(String userId){
        page = 1;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.userJoinActivity(userId, "", page, 10)).setData(userJoinActivityLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 用户参加的活动
     *
     */
    public void userJoinActivityMore(String userId){
        page ++;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.userJoinActivity(userId, "", page, 10)).setData(userJoinActivityLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 活动列表
     *
     * @param classId
     */
    public void activityList(String classId){
        page = 1;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.activityList(classId,"", page, 10)).setData(activityListLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 活动列表
     *
     * @param classId
     */
    public void activityListMore(String classId){
        page ++;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.activityList(classId,"", page, 10)).setData(activityListLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }
}
