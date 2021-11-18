package com.queerlab.chat.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.queerlab.chat.base.PageState;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.bean.ActivityStatusBean;
import com.queerlab.chat.bean.GroupListBean;
import com.queerlab.chat.bean.GroupRoomIdBean;
import com.queerlab.chat.bean.LocationUserBean;
import com.queerlab.chat.http.retrofit.BaseRepository;
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSendCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.viewmodel
 * @ClassName: GroupViewModel
 * @Description: 小组ViewModel
 * @Author: 鹿鸿祥
 * @CreateDate: 6/4/21 9:18 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 6/4/21 9:18 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class GroupViewModel extends BaseRepository {
    public MutableLiveData<String> pageStateLiveData;
    public MutableLiveData<String> failStateLiveData;
    public MutableLiveData<GroupListBean> groupListLiveData;
    public MutableLiveData<GroupListBean> groupLiveListLiveData;
    public MutableLiveData<GroupListBean> userLatelyGroupLiveData;
    public MutableLiveData<GroupListBean> groupSearchListLiveData;
    public MutableLiveData groupAddLiveData;
    public MutableLiveData groupJoinLiveData;
    public MutableLiveData groupDissolutionLiveData;
    public MutableLiveData groupLogoutLiveData;
    public MutableLiveData groupKickingLiveData;
    public MutableLiveData<List<LocationUserBean.ListBean>> groupMemberLiveData;
    public MutableLiveData voiceRoomCreateLiveData;
    public MutableLiveData voiceRoomDeleteLiveData;
    public MutableLiveData voiceRoomJoinLiveData;
    public MutableLiveData voiceRoomLogoutLiveData;
    public MutableLiveData<GroupRoomIdBean> getGroupRoomIdLiveData;
    public MutableLiveData<ActivityStatusBean> activityStatusLiveData;
    private final String userId;
    private int page = 1;
    private int pageLive = 1;

    public GroupViewModel() {
        userId = SPUtils.getInstance().getString(SpConfig.USER_ID);
        pageStateLiveData = new MutableLiveData<>();
        failStateLiveData = new MutableLiveData<>();
        groupListLiveData = new MutableLiveData();
        groupLiveListLiveData = new MutableLiveData<>();
        userLatelyGroupLiveData = new MutableLiveData<>();
        groupSearchListLiveData = new MutableLiveData<>();
        groupAddLiveData = new MutableLiveData();
        groupJoinLiveData = new MutableLiveData();
        groupDissolutionLiveData = new MutableLiveData();
        groupLogoutLiveData = new MutableLiveData();
        groupKickingLiveData = new MutableLiveData();
        groupMemberLiveData = new MutableLiveData<>();
        voiceRoomCreateLiveData = new MutableLiveData();
        voiceRoomDeleteLiveData = new MutableLiveData();
        voiceRoomJoinLiveData = new MutableLiveData();
        voiceRoomLogoutLiveData = new MutableLiveData();
        getGroupRoomIdLiveData = new MutableLiveData();
        activityStatusLiveData = new MutableLiveData<>();
    }

    /**
     * 群聊列表
     * 群聊分类 1 live/ null推荐
     */
    public void groupList(String classId){
        page = 1;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.groupList(userId, classId, page ,10)).setData(groupListLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 群聊列表
     */
    public void groupListMore(String classId){
        page ++;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.groupList(userId, classId, page ,10)).setData(groupListLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 群聊直播列表
     */
    public void groupLiveList(){
        pageLive = 1;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.groupList(userId, "1", pageLive ,10)).setData(groupLiveListLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 群聊直播列表
     */
    public void groupLiveListMore(){
        pageLive ++;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.groupList(userId, "1", pageLive ,10)).setData(groupLiveListLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 用户去过的群聊
     */
    public void userLatelyGroupList(String userId){
        page = 1;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.userLatelyGroupList(userId, page ,5)).setData(userLatelyGroupLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 用户去过的群聊
     */
    public void userLatelyGroupListMore(String userId){
        page ++;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.userLatelyGroupList(userId, page ,10)).setData(userLatelyGroupLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 群聊搜索列表
     */
    public void groupSearchList(String groupName){
        page = 1;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.groupSearchList(userId, groupName, page ,10)).setData(groupSearchListLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 群聊搜索列表
     */
    public void groupSearchListMore(String groupName){
        page ++;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.groupSearchList(userId, groupName, page ,10)).setData(groupSearchListLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 创建小组
     *
     * @param groupNo 小组ID
     * @param groupName 小组名称
     * @param groupType 小组标识
     * @param classId 分类ID
     */
    public void groupAdd(String groupNo, String groupName, String groupType, String classId){
        request(apiService.groupAdd(groupNo, groupName, groupType ,userId, classId)).setData(groupAddLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 用户加入小组
     *
     * @param groupNo
     */
    public void groupJoin(String groupNo){
        request(apiService.groupJoin(groupNo ,userId)).setData(groupJoinLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 用户解散小组
     *
     * @param groupNo
     */
    public void groupDissolution(String groupNo){
        request(apiService.groupDissolution(groupNo)).setData(groupDissolutionLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 用户退出小组
     *
     * @param groupNo
     */
    public void groupLogout(String groupNo){
        request(apiService.groupLogout(groupNo ,userId)).setData(groupLogoutLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 群主移出用户
     *
     * @param groupNo
     */
    public void groupKicking(String groupNo, String userId){
        request(apiService.groupKicking(groupNo ,userId)).setData(groupKickingLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 获取群聊成员列表
     *
     * @param groupId
     */
    public void groupMemberList(String groupId){
        V2TIMManager.getGroupManager().getGroupMemberList(groupId, V2TIMGroupMemberFullInfo.V2TIM_GROUP_MEMBER_FILTER_ALL, 0, new V2TIMSendCallback<V2TIMGroupMemberInfoResult>() {
            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onError(int code, String desc) {
                LogUtils.e("groupMemberList = "  + code + " , errorInfo =  + " + desc);
            }

            @Override
            public void onSuccess(V2TIMGroupMemberInfoResult v2TIMGroupMemberInfoResult) {
                List<LocationUserBean.ListBean> list = new ArrayList<>();
                for (V2TIMGroupMemberFullInfo v2TIMGroupMemberFullInfo : v2TIMGroupMemberInfoResult.getMemberInfoList()) {
                    LocationUserBean.ListBean listBean = new LocationUserBean.ListBean();
                    listBean.setUser_id(Integer.parseInt(v2TIMGroupMemberFullInfo.getUserID()));
                    listBean.setUser_name(v2TIMGroupMemberFullInfo.getNickName());
                    listBean.setUser_portrait(v2TIMGroupMemberFullInfo.getFaceUrl());
                    list.add(listBean);
                }
                groupMemberLiveData.postValue(list);
            }
        });
    }

    /**
     * 群主创建语音聊天室
     *
     * @param groupNo
     * @param roomId
     * @param userId
     */
    public void voiceRoomCreate(String groupNo, int roomId, String userId){
        request(apiService.voiceRoomCreate(groupNo, roomId ,userId)).setData(voiceRoomCreateLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 群主解散语音聊天室
     *
     * @param groupNo
     */
    public void voiceRoomDelete(String groupNo){
        request(apiService.voiceRoomDelete(groupNo)).setData(voiceRoomDeleteLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 用户加入语音聊天室
     *
     * @param groupNo
     * @param userId
     */
    public void voiceRoomJoin(String groupNo, String userId){
        request(apiService.voiceRoomJoin(groupNo ,userId)).setData(voiceRoomJoinLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 用户退出语音聊天室
     *
     * @param groupNo
     * @param userId
     */
    public void voiceRoomLogout(String groupNo, String userId){
        request(apiService.voiceRoomLogout(groupNo ,userId)).setData(voiceRoomLogoutLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 获取当前群聊的roomID
     *
     * @param groupNo
     */
    public void getGroupRoomId(String groupNo){
        request(apiService.getGroupRoomId(groupNo)).setData(getGroupRoomIdLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 查询当前群聊是否关联活动
     *
     * @param groupNo
     */
    public void selectActivityStatus(String groupNo){
        request(apiService.selectActivityStatus(groupNo)).setData(activityStatusLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }
}
