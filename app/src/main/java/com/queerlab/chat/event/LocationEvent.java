package com.queerlab.chat.event;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.event
 * @ClassName: LocationEvent
 * @Description: java类作用描述
 * @Author: 鹿鸿祥
 * @CreateDate: 5/27/21 1:16 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/27/21 1:16 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LocationEvent {

    double longitude;
    double latitude;
    String status;

    public LocationEvent(String status) {
        this.status = status;
    }

    public LocationEvent(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public LocationEvent(double longitude, double latitude, String status) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
