package com.queerlab.chat.bean;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.bean
 * @ClassName: HeatMapListBean
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 5/27/21 3:44 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/27/21 3:44 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class HeatMapListBean {
    /**
     * lng : 108
     * lat : 30
     * userNum : 10
     */

    private String lng;
    private String lat;
    private String userNum;

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }
}
