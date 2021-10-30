package com.queerlab.chat.http.retrofit;

import androidx.lifecycle.MutableLiveData;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.queerlab.chat.R;
import com.queerlab.chat.base.PageState;
import com.queerlab.chat.http.entity.BaseListResponse;
import com.queerlab.chat.http.entity.BaseResponse;
import com.queerlab.chat.http.error.ApiException;
import com.queerlab.chat.http.error.ErrorHandlerFactory;
import com.queerlab.chat.http.error.ResponseErrorListenerImpl;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.http.retrofit
 * @ClassName: BaseSubscriber
 * @Description: subscriber封装
 * @Author: 鹿鸿祥
 * @CreateDate: 2020/8/31 16:16
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/8/31 16:16
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class BaseSubscriber<T> implements Subscriber<T> {

    private ErrorHandlerFactory mErrorHandlerFactory;
    private MutableLiveData<T> data;
    private MutableLiveData<List<T>> dataList;
    private MutableLiveData<String> pageState;
    private MutableLiveData<String> failStatue;

    public BaseSubscriber() {
        this.mErrorHandlerFactory = new ErrorHandlerFactory(new ResponseErrorListenerImpl());
    }

    /**
     * 获取请求过程状态，更新界面
     * @return
     */
    public void setPageState(MutableLiveData<String> pageState) {
        this.pageState = pageState;
    }

    /**
     * 接口请求失败返回 数据结果
     *
     * @param failStatue
     */
    public void setFailStatue(MutableLiveData<String> failStatue){
        this.failStatue = failStatue;
    }

    /**
     * 返回数据类型BaseResponse
     * @return
     */
    public void setData(MutableLiveData<T> data) {
        this.data = data;
    }

    /**
     * 返回数据类型BaseListResponse
     * @return
     */
    public void setDataList(MutableLiveData<List<T>> dataList){
        this.dataList = dataList;
    }

    public void set(T t){
        if (t instanceof BaseResponse && this.data != null){
            this.data.setValue((T) ((BaseResponse) t).getData());
        }else if (t instanceof BaseListResponse && this.dataList != null){
            this.dataList.setValue(((BaseListResponse) t).getData());
        }
    }

    public void onFinish(T t){
        set(t);
    }

    @Override
    public void onSubscribe(Subscription s) {
        //观察者接收事件 = 1个
        s.request(1);
        //网络判断，无网络停止请求
        if (!NetworkUtils.isConnected()){
            ToastUtils.showShort(R.string.net_not);
            if (pageState != null) pageState.postValue(PageState.PAGE_NET_ERROR);
            if (failStatue != null) failStatue.postValue("");
            s.cancel();
            return;
        }
        if (pageState != null && !PageState.PAGE_REFRESH.equals(pageState.getValue()) && !PageState.PAGE_LOADING.equals(pageState.getValue())){
            pageState.postValue(PageState.PAGE_LOADING);
        }
    }

    @Override
    public void onNext(T t) {
        if (t instanceof BaseResponse){
            if (((BaseResponse) t).getCode() == 200){
                onFinish(t);
                if (pageState != null) pageState.postValue(PageState.PAGE_LOAD_SUCCESS);
            }else {
                mErrorHandlerFactory.handleError(new ApiException(((BaseResponse) t).getCode(), ((BaseResponse) t).getMsg()));
                if (pageState != null) pageState.postValue(PageState.PAGE_SERVICE_ERROR);
                if (failStatue != null) failStatue.postValue(((BaseResponse) t).getMsg());
            }
        }else if (t instanceof BaseListResponse){
            if (((BaseListResponse) t).getCode() == 200){
                onFinish(t);
                if (pageState != null) pageState.postValue(PageState.PAGE_LOAD_SUCCESS);
            }else {
                mErrorHandlerFactory.handleError(new ApiException(((BaseListResponse) t).getCode(), ((BaseListResponse) t).getMsg()));
                if (pageState != null) pageState.postValue(PageState.PAGE_SERVICE_ERROR);
                if (failStatue != null) failStatue.postValue(((BaseResponse) t).getMsg());
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        mErrorHandlerFactory.handleError(e);
        if (pageState != null) pageState.postValue(PageState.PAGE_ERROR);
        if (failStatue != null) failStatue.postValue("请求网络数据异常");
    }

    @Override
    public void onComplete() {

    }
}
