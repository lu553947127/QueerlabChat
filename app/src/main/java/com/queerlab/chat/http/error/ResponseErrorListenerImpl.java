package com.queerlab.chat.http.error;

import android.net.ParseException;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.queerlab.chat.base.SpConfig;

import org.json.JSONException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import retrofit2.HttpException;
/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.http.error
 * @ClassName: ResponseErrorListener
 * @Description: 网络请求错误信息
 * @Author: 鹿鸿祥
 * @CreateDate: 2020/8/31 09:57
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/8/31 09:57
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ResponseErrorListenerImpl implements ResponseErrorListener {
    @Override
    public void handlerResponseError(Throwable t) {
        LogUtils.i("请求出错", t.getMessage(), t.getCause());
        String msg = "未知错误";
        if (t instanceof UnknownHostException) {
            msg = "网络不可用";
        } else if (t instanceof SocketTimeoutException) {
            msg = "请求网络超时";
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            msg = httpException.getMessage();
        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "数据解析错误";
        }else if (t instanceof ApiException){
            //自定义错误
            msg = convertStatusCode((ApiException) t);
        }
        if (!TextUtils.isEmpty(msg)){
            ToastUtils.showLong(msg);
        }
    }

    /**
     * 自定义错误处理，非200
     *  可根据后台提供错误码信息
     */
    private String convertStatusCode(ApiException exception) {
        String msg;
        if (exception.getErrorCode() == 9001) {
            msg = "token已失效！";
            SPUtils.getInstance().remove(SpConfig.TOKEN);
        }else if (exception.getErrorCode() == 6009){
            msg = "";
//            LoginUtils.getExitLogin();
        }else {
            msg = exception.getMsg();
        }
        return msg;
    }
}
