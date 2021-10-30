package com.queerlab.chat.bean;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.bean
 * @ClassName: LoginBean
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 5/24/21 9:38 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/24/21 9:38 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LoginBean {
    /**
     * userStatus : 2
     * userSig : eJyrVgrxCdZLrSjILEpVsjIzMLEwMNABi5WlFilZKRnpGShB*MUp2YkFBZkpSlaGJgYGpgamhmZGEJnMlNS8ksy0TLAGQ5jyzHQgzzMlLCUnxMDS1THYwtkgqdwnMKvQ26ygqsQy1znEING3xKSw0rQyIzfPJNkWqrEkMxfoEqDhJkbG5pZmprUA9JYvSA__
     * userName : 老大哥
     * userPortrait : https://tdc-1303184599.cos.na-ashburn.myqcloud.com/head_portrait/25193d97-db3f-4ef9-9f16-f650fcf17c68.jpg
     * userId : 1
     * token : 41650c011ea04fceacb00a79af5760f0
     */

    private String userStatus;
    private String userSig;
    private String userName;
    private String userPortrait;
    private String userId;
    private String token;

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPortrait() {
        return userPortrait;
    }

    public void setUserPortrait(String userPortrait) {
        this.userPortrait = userPortrait;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
