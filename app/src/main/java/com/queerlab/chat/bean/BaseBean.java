package com.queerlab.chat.bean;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.bean
 * @ClassName: BaseBean
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 5/11/21 9:27 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/11/21 9:27 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class BaseBean {

    String tag;
    String title;

    private double latitude;
    private double longitude;
    private int markerDrawable;

    public BaseBean(String tag, String title, double latitude, double longitude, int markerDrawable) {
        this.tag = tag;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.markerDrawable = markerDrawable;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getMarkerDrawable() {
        return markerDrawable;
    }

    public void setMarkerDrawable(int markerDrawable) {
        this.markerDrawable = markerDrawable;
    }

    public BaseBean(String tag, String title) {
        this.tag = tag;
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
