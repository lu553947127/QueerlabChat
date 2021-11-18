package com.queerlab.chat.http.api;

import com.queerlab.chat.bean.ActivityListBean;
import com.queerlab.chat.bean.ActivityStatusBean;
import com.queerlab.chat.bean.GroupEmoBean;
import com.queerlab.chat.bean.GroupRoomIdBean;
import com.queerlab.chat.bean.GroupTypeBean;
import com.queerlab.chat.bean.HeatMapListBean;
import com.queerlab.chat.bean.LocationUserBean;
import com.queerlab.chat.bean.LoginBean;
import com.queerlab.chat.bean.GroupListBean;
import com.queerlab.chat.bean.NoticeBean;
import com.queerlab.chat.bean.UploadFileBean;
import com.queerlab.chat.bean.UserInfoBean;
import com.queerlab.chat.http.entity.BaseListResponse;
import com.queerlab.chat.http.entity.BaseResponse;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.http.api
 * @ClassName: ApiService
 * @Description: 服务器接口集合
 * @Author: 鹿鸿祥
 * @CreateDate: 2020/8/31 16:04
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/8/31 16:04
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface ApiService {

    //获取验证码
    @FormUrlEncoded
    @POST("/user/sendSms")
    Flowable<BaseResponse> sendSms(
            @Field("countryCode") String countryCode,
            @Field("phoneNumber") String phoneNumber
    );

    //验证码登录
    @FormUrlEncoded
    @POST("/user/queryUserByPhone")
    Flowable<BaseResponse<LoginBean>> login(
            @Field("countryCode") String countryCode,
            @Field("phoneNumber") String phoneNumber,
            @Field("code") String code
    );

    //用户信息添加
    @FormUrlEncoded
    @POST("/user/add")
    Flowable<BaseResponse<LoginBean>> userAdd(
            @Field("userName") String userName,
            @Field("countryCode") String countryCode,
            @Field("userPhone") String phoneNumber,
            @Field("userType") String userType
    );

    //用户上传定位
    @FormUrlEncoded
    @POST("/user/update")
    Flowable<BaseResponse> updateLocation(
            @Field("userId") String userId,
            @Field("lng") String lng,
            @Field("lat") String lat
    );

    //获取热力图数据列表
    @POST("/user/queryUserNumByLngLat")
    Flowable<BaseListResponse<HeatMapListBean>> queryUserNumByLngLat();

    //根据定位查询用户列表
    @FormUrlEncoded
    @POST("/user/queryUserByLngLat")
    Flowable<BaseResponse<LocationUserBean>> locationUser(
            @Field("lng") String lng,
            @Field("lat") String lat,
            @Field("page") int page,
            @Field("rows") int rows
    );

    //用户详情
    @FormUrlEncoded
    @POST("/user/queryUserById")
    Flowable<BaseResponse<UserInfoBean>> userInfo(
            @Field("userId") String userId
    );

    //上传头像
    @Multipart
    @POST("/user/uploadFile")
    Flowable<BaseResponse<UploadFileBean>> uploadFile(
            @Query("userId") String userId,
            @Part MultipartBody.Part userPortrait
    );

    //更新状态
    @FormUrlEncoded
    @POST("/user/update")
    Flowable<BaseResponse> updateStatus(
            @Field("userId") String userId,
            @Field("userStatus") String userStatus
    );

    //修改昵称
    @FormUrlEncoded
    @POST("/user/update")
    Flowable<BaseResponse> updateName(
            @Field("userId") String userId,
            @Field("userName") String userName
    );

    //编辑兴趣
    @FormUrlEncoded
    @POST("/user/update")
    Flowable<BaseResponse> updateType(
            @Field("userId") String userId,
            @Field("userType") String userType
    );

    //隐藏去过的小组
    @FormUrlEncoded
    @POST("/user/update")
    Flowable<BaseResponse> updateHideGroup(
            @Field("userId") String userId,
            @Field("isHideGroup") String isHideGroup
    );

    //隐藏参加的活动
    @FormUrlEncoded
    @POST("/user/update")
    Flowable<BaseResponse> updateHideActivity(
            @Field("userId") String userId,
            @Field("isHideActivity") String isHideActivity
    );

    //用户拍一拍
    @FormUrlEncoded
    @POST("/user/updateUserPaiPai")
    Flowable<BaseResponse> updateUserClap(
            @Field("userId") String userId
    );

    //用户搜索列表
    @FormUrlEncoded
    @POST("/user/queryUserByName")
    Flowable<BaseResponse<LocationUserBean>> userSearch(
            @Field("userName") String userName,
            @Field("page") int page,
            @Field("rows") int rows
    );

    //用户去过的群聊
    @FormUrlEncoded
    @POST("/group/queryGroupByUserTime")
    Flowable<BaseResponse<GroupListBean>> userLatelyGroupList(
            @Field("userId") String userId,
            @Field("page") int page,
            @Field("rows") int rows
    );

    //小组列表
    @FormUrlEncoded
    @POST("/group/queryAllGroup")
    Flowable<BaseResponse<GroupListBean>> groupList(
            @Field("userId") String userId,
            @Field("classId") String classId,
            @Field("page") int page,
            @Field("rows") int rows
    );

    //小组搜索列表
    @FormUrlEncoded
    @POST("/group/queryAllGroupLike")
    Flowable<BaseResponse<GroupListBean>> groupSearchList(
            @Field("userId") String userId,
            @Field("groupName") String groupName,
            @Field("page") int page,
            @Field("rows") int rows
    );

    //创建小组
    @FormUrlEncoded
    @POST("/group/groupAdd")
    Flowable<BaseResponse> groupAdd(
            @Field("groupNo") String groupNo,
            @Field("groupName") String groupName,
            @Field("groupType") String groupType,
            @Field("userId") String userId,
            @Field("classId") String classId
    );

    //用户加入小组
    @FormUrlEncoded
    @POST("/group/addGroupByUser")
    Flowable<BaseResponse> groupJoin(
            @Field("groupNo") String groupNo,
            @Field("userId") String userId
    );

    //解散小组
    @FormUrlEncoded
    @POST("/group/deleteGroupById")
    Flowable<BaseResponse> groupDissolution(
            @Field("groupNo") String groupNo
    );

    //退出小组
    @FormUrlEncoded
    @POST("/group/deleteGroupByUser")
    Flowable<BaseResponse> groupLogout(
            @Field("groupNo") String groupNo,
            @Field("userId") String userId
    );

    //群主移出用户
    @FormUrlEncoded
    @POST("/group/groupBlockByUser")
    Flowable<BaseResponse> groupKicking(
            @Field("groupNo") String groupNo,
            @Field("userId") String userId
    );

    //群主创建语音房间
    @FormUrlEncoded
    @POST("/group/updateGroupLiveByUser")
    Flowable<BaseResponse> voiceRoomCreate(
            @Field("groupNo") String groupNo,
            @Field("roomId") int roomId,
            @Field("userId") String userId
    );

    //群主解散语音房间
    @FormUrlEncoded
    @POST("/group/updateRelationshipLive")
    Flowable<BaseResponse> voiceRoomDelete(
            @Field("groupNo") String groupNo
    );

    //用户加入语音房间
    @FormUrlEncoded
    @POST("/group/addRelationshipUserLive")
    Flowable<BaseResponse> voiceRoomJoin(
            @Field("groupNo") String groupNo,
            @Field("userId") String userId
    );

    //用户退出语音房间
    @FormUrlEncoded
    @POST("/group/updateRelationshipUserLive")
    Flowable<BaseResponse> voiceRoomLogout(
            @Field("groupNo") String groupNo,
            @Field("userId") String userId
    );

    //获取当前群聊的roomId
    @FormUrlEncoded
    @POST("/group/queryRoomId")
    Flowable<BaseResponse<GroupRoomIdBean>> getGroupRoomId(
            @Field("groupNo") String groupNo
    );

    //查询当前群聊是否关联活动
    @FormUrlEncoded
    @POST("/activity/app/selectActivityStatus")
    Flowable<BaseResponse<ActivityStatusBean>> selectActivityStatus(
            @Field("groupNo") String groupNo
    );

    //获取小组/活动类型
    @FormUrlEncoded
    @POST("/class/app/queryAllClass")
    Flowable<BaseResponse<GroupTypeBean>> getGroupType(
            @Field("type") String type,
            @Field("page") int page,
            @Field("rows") int rows
    );

    //获取小组emo表情
    @FormUrlEncoded
    @POST("/class/app/queryAllEmo")
    Flowable<BaseResponse<GroupEmoBean>> getGroupEmo(
            @Field("page") int page,
            @Field("rows") int rows
    );

    //根据经纬度活动列表
    @FormUrlEncoded
    @POST("/activity/app/selectActivity")
    Flowable<BaseResponse<ActivityListBean>> locationActivity(
            @Field("lng") String lng,
            @Field("lat") String lat,
            @Field("page") int page,
            @Field("rows") int rows
    );

    //活动列表根据关键字搜索
    @FormUrlEncoded
    @POST("/activity/app/selectActivity")
    Flowable<BaseResponse<ActivityListBean>> SearchActivity(
            @Field("title") String title,
            @Field("page") int page,
            @Field("rows") int rows
    );

    //用户参加的活动
    @FormUrlEncoded
    @POST("/activity/app/selectActivity")
    Flowable<BaseResponse<ActivityListBean>> userJoinActivity(
            @Field("userId") String userId,
            @Field("page") int page,
            @Field("rows") int rows
    );

    //活动列表
    @FormUrlEncoded
    @POST("/activity/app/selectActivity")
    Flowable<BaseResponse<ActivityListBean>> activityList(
            @Field("classId") String classId,
            @Field("page") int page,
            @Field("rows") int rows
    );

    //系统通知
    @POST("/notice/queryNotice")
    Flowable<BaseResponse<NoticeBean>> notice();
}
