package com.queerlab.chat.utils;

import android.location.Location;
import android.os.Looper;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.queerlab.chat.app.MyApplication;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.event.LocationEvent;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.LocationSource;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;

import org.greenrobot.eventbus.EventBus;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.utils
 * @ClassName: LocationUtils
 * @Description: 腾讯地图定位工具类
 * @Author: 鹿鸿祥
 * @CreateDate: 5/20/21 11:04 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/20/21 11:04 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LocationUtils implements TencentLocationListener, LocationSource {
    private TencentLocationManager locationManager;
    private TencentLocationRequest locationRequest;
    private OnLocationChangedListener locationChangedListener;
    private TencentMap mTencentMap;

    private static class LocationHolder {
        private static final LocationUtils INSTANCE = new LocationUtils();
    }

    public static LocationUtils getInstance() {
        return LocationHolder.INSTANCE;
    }

    /**
     * 开启定位服务
     */
    public void startLocalService(TencentMap mTencentMap) {
        this.mTencentMap = mTencentMap;
        //用于访问腾讯定位服务的类, 周期性向客户端提供位置更新
        locationManager = TencentLocationManager.getInstance(MyApplication.getInstance());
        //设置坐标系
        locationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        //创建定位请求
        locationRequest = TencentLocationRequest.create();
        //设置定位周期（位置监听器回调周期）为3s
        locationRequest.setInterval(3000);
        //地图上设置定位数据源
        mTencentMap.setLocationSource(this);
        //设置当前位置可见
        mTencentMap.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if (i == TencentLocation.ERROR_OK && locationChangedListener != null) {
            Location location = new Location(tencentLocation.getProvider());
            //设置经纬度以及精度
            location.setLatitude(tencentLocation.getLatitude());
            location.setLongitude(tencentLocation.getLongitude());
            location.setAccuracy(tencentLocation.getAccuracy());
            locationChangedListener.onLocationChanged(location);
//            LogUtils.e( "tencentLocation："+tencentLocation.getLongitude()+"======latitude："+tencentLocation.getLatitude());
            EventBus.getDefault().post(new LocationEvent(tencentLocation.getLongitude(), tencentLocation.getLatitude(), "success"));
            SPUtils.getInstance().put(SpConfig.LONGITUDE, String.valueOf(tencentLocation.getLongitude()), true);
            SPUtils.getInstance().put(SpConfig.LATITUDE, String.valueOf(tencentLocation.getLatitude()), true);
            mTencentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new com.tencent.tencentmap.mapsdk.maps.model.LatLng(tencentLocation.getLatitude(), tencentLocation.getLongitude()), 10.946870f));
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {
//        LogUtils.e("onStatusUpdate", s + "===" + s1);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        locationChangedListener = onLocationChangedListener;
        int err = locationManager.requestLocationUpdates(locationRequest, this, Looper.myLooper());
        switch (err) {
            case 1:
                LogUtils.e("设备缺少使用腾讯定位服务需要的基本条件");
                break;
            case 2:
                LogUtils.e("manifest 中配置的 key 不正确");
                break;
            case 3:
                LogUtils.e("动加载libtencentloc.so失败");
                break;
            default:
                break;
        }
    }

    @Override
    public void deactivate() {
        if (locationManager != null){
            locationManager.removeUpdates(this);
            locationManager = null;
        }

        if (locationRequest != null){
            locationRequest = null;
        }

     if (locationChangedListener != null){
         locationChangedListener = null;
     }
    }

    /**
     * 停止定位
     */
    public void stopLocalService() {
        deactivate();
    }
}
