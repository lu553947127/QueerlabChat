package com.queerlab.chat.bean;

import java.io.Serializable;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.bean
 * @ClassName: UserInfoBean
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 5/31/21 9:10 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/31/21 9:10 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class UserInfoBean implements Serializable {
    /**
     * user_status : 忙碌1
     * phone_country_code : 86
     * lng : 118.303000
     * user_name : 用户1
     * paipai : 4
     * user_sign : 我还是从前那个少年1
     * is_hide_group : 2
     * user_type : 兴趣1,兴趣2,兴趣3,兴趣4
     * user_portrait : https://tdc-1303184599.cos.na-ashburn.myqcloud.com/head_portrait/1546cd3a-caad-42e6-8969-bc09feb7f603.jpg
     * user_id : 1
     * user_phone : 15564559243
     * user_portrait_key : /head_portrait/1546cd3a-caad-42e6-8969-bc09feb7f603.jpg
     * lat : 33.909000
     */

    private String user_status;
    private String phone_country_code;
    private String lng;
    private String user_name;
    private int paipai;
    private String user_sign;
    private int is_hide_group;
    private String user_type;
    private String user_portrait;
    private int user_id;
    private String user_phone;
    private String user_portrait_key;
    private String lat;

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getPhone_country_code() {
        return phone_country_code;
    }

    public void setPhone_country_code(String phone_country_code) {
        this.phone_country_code = phone_country_code;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getPaipai() {
        return paipai;
    }

    public void setPaipai(int paipai) {
        this.paipai = paipai;
    }

    public String getUser_sign() {
        return user_sign;
    }

    public void setUser_sign(String user_sign) {
        this.user_sign = user_sign;
    }

    public int getIs_hide_group() {
        return is_hide_group;
    }

    public void setIs_hide_group(int is_hide_group) {
        this.is_hide_group = is_hide_group;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_portrait() {
        return user_portrait;
    }

    public void setUser_portrait(String user_portrait) {
        this.user_portrait = user_portrait;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_portrait_key() {
        return user_portrait_key;
    }

    public void setUser_portrait_key(String user_portrait_key) {
        this.user_portrait_key = user_portrait_key;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
