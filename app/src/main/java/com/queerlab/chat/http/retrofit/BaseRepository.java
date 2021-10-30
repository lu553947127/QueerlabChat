package com.queerlab.chat.http.retrofit;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.queerlab.chat.app.MyApplication;
import com.queerlab.chat.http.api.ApiService;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.http.retrofit
 * @ClassName: BaseRepository
 * @Description: 网络请求处理
 * @Author: 鹿鸿祥
 * @CreateDate: 2020/8/31 18:10
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/8/31 18:10
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class BaseRepository<T> extends ViewModel {

    /*RxJava回调*/
    private BaseSubscriber<T> baseObservable;
    /*解决背压*/
    private Flowable<T> flowable;
    public ApiService apiService;

    private CompositeDisposable compositeDisposable;//RxJava

    /*初始化*/
    public BaseRepository() {
        if (this.baseObservable == null)
            this.baseObservable = new BaseSubscriber<T>();
        if (this.apiService == null)
            this.apiService = MyApplication.baseHttpRequest.getApiService();
    }

    /**
     * 发送请求
     *
     * @return
     */
    public BaseSubscriber<T> send() {
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObservable);
        return baseObservable;
    }

    /**
     * 初始化flowable
     *
     * @param flowable
     * @return
     */
    public BaseRepository<T> request(Flowable<T> flowable) {
        this.flowable = flowable;
        return this;
    }

    /**
     * 获取请求过程状态，更新界面
     *
     * @return
     */
    public BaseRepository<T> setPageState(MutableLiveData<String> pageState) {
        this.baseObservable.setPageState(pageState);
        return this;
    }

    /**
     * 接口请求失败返回 数据结果
     *
     * @return
     */
    public BaseRepository<T> setFailStatue(MutableLiveData<String> failStatue) {
        this.baseObservable.setFailStatue(failStatue);
        return this;
    }

    /**
     * 返回数据类型BaseResponse
     * data为 object
     * 后台数据格式
     * {
     * "status": "success",
     * "code": 10001,
     * "message": "操作成功",
     * "data": {
     * <p>
     * }
     * }
     *
     * @return
     */
    public BaseRepository<T> setData(MutableLiveData<T> data) {
        this.baseObservable.setData(data);
        return this;
    }

    /**
     * 返回数据类型BaseListResponse
     * data为 数组集合
     * 后台数据格式
     * {
     * "status": "success",
     * "code": 10001,
     * "message": "操作成功",
     * "data": {
     * <p>
     * }
     * }
     *
     * @return
     */
    public BaseRepository<T> setDataList(MutableLiveData<List<T>> dataList) {
        this.baseObservable.setDataList(dataList);
        return this;
    }

    @Override
    protected void onCleared() {
        if (compositeDisposable != null){
            compositeDisposable.clear();
        }
        super.onCleared();
    }

    /**
     * 将每一个Disposable保存，clear方法清空
     * @param disposable
     */
    protected void addDisposable(Disposable disposable){
        if (compositeDisposable == null){
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }
}
