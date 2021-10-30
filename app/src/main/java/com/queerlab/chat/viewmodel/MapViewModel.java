package com.queerlab.chat.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.SPUtils;
import com.queerlab.chat.base.PageState;
import com.queerlab.chat.base.SpConfig;
import com.queerlab.chat.bean.HeatMapListBean;
import com.queerlab.chat.bean.LocationUserBean;
import com.queerlab.chat.http.retrofit.BaseRepository;

import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.viewmodel
 * @ClassName: MapViewModel
 * @Description: 地图ViewModel
 * @Author: 鹿鸿祥
 * @CreateDate: 5/27/21 10:28 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 5/27/21 10:28 AM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MapViewModel extends BaseRepository {
    public MutableLiveData<String> pageStateLiveData;
    public MutableLiveData<String> failStateLiveData;
    public MutableLiveData<String> updateLocationLiveData;
    public MutableLiveData<List<HeatMapListBean>> heatMapListLiveData;
    public MutableLiveData<LocationUserBean> locationUserLiveData;
    private final String userId;
    private int page = 1;

    public MapViewModel() {
        userId = SPUtils.getInstance().getString(SpConfig.USER_ID);
        pageStateLiveData = new MutableLiveData<>();
        failStateLiveData = new MutableLiveData<>();
        updateLocationLiveData = new MutableLiveData<>();
        heatMapListLiveData = new MutableLiveData<>();
        locationUserLiveData = new MutableLiveData();
    }

    /**
     * 用户上传经纬度
     *
     * @param longitude 经度
     * @param latitude 纬度
     */
    public void updateLocation(String longitude, String latitude) {
        request(apiService.updateLocation(userId, longitude, latitude)).setData(updateLocationLiveData).setPageState(pageStateLiveData).setFailStatue(failStateLiveData).send();
    }

    /**
     * 获取热力图列表数据
     */
    public void heatMapList(){
        request(apiService.queryUserNumByLngLat()).setDataList(heatMapListLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 根据定位查询用户列表
     */
    public void locationUser(String longitude, String latitude){
        page = 1;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.locationUser(longitude, latitude ,page ,10)).setData(locationUserLiveData).setPageState(pageStateLiveData).send();
    }

    /**
     * 根据定位查询用户列表
     */
    public void locationUserMore(String longitude, String latitude){
        page ++;
        pageStateLiveData.postValue(PageState.PAGE_REFRESH);
        request(apiService.locationUser(longitude, latitude ,page ,10)).setData(locationUserLiveData).setPageState(pageStateLiveData).send();
    }
}
