package com.queerlab.chat.http.retrofit;

import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.queerlab.chat.base.SpConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.http.utils
 * @ClassName: HttpInterceptorUtil
 * @Description: OkHttpClient网络请求拦截器自定义
 * @Author: XuYu
 * @CreateDate: 2020/8/3 10:03
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/8/3 10:03
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class HttpInterceptorUtil {
    public static final String TAG = "HttpInterceptorUtil";

    //日志拦截器,用于打印返回请求的结果
    public static HttpLoggingInterceptor LogInterceptor() {
        return new HttpLoggingInterceptor(message -> LogUtils.d(TAG, message)).setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    //绑定header拦截器
    public static Interceptor HeaderInterceptor() {
        return chain -> {
            //添加请求头
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder().addHeader("token", SPUtils.getInstance().getString(SpConfig.TOKEN));
            Request request = requestBuilder.build();
//                LogUtils.e(SPUtils.getInstance().getString(SpConfig.TOKEN));
            return chain.proceed(request);
        };
    }

    //数据加载拦截器
    public static Interceptor NullResponseInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                Response.Builder builder = response.newBuilder();
                Response clone = builder.build();
                ResponseBody body = clone.body();

                if (response.code() == 200) {
                    MediaType mediaType = response.body().contentType();
                    String responseJson = response.body().string();
                    String data = JsonUtils.getString(responseJson, "data");
                    String msg = JsonUtils.getString(responseJson, "msg");
                    if (data.equals("")) {
//                        if (!msg.equals("成功！")) {
//                            ToastUtils.showShort(msg);
//                        }
                    }
                    body = ResponseBody.create(mediaType, responseJson);
                    return response.newBuilder()
                            .body(body)
                            .build();
                }
                return response;
            }
        };
    }
}
