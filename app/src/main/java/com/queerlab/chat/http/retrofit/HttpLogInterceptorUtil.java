package com.queerlab.chat.http.retrofit;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.http.retrofit
 * @ClassName: HttpLogInterceptorUtil
 * @Description: 网络请求数据返回日志，自定义格式打印，方便查看
 * @Author: 鹿鸿祥
 * @CreateDate: 2020/8/31 17:07
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/8/31 17:07
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class HttpLogInterceptorUtil implements Interceptor {

    private final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();

        String body = null;

        if (requestBody != null){
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null){
                charset = contentType.charset(UTF8);
            }
            body = buffer.readString(charset);
        }

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        String rBody = null;

        if (responseBody != null){
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null){
                try {
                    charset = contentType.charset(UTF8);
                }catch (UnsupportedCharsetException e){
                    e.printStackTrace();
                }
            }

            rBody = buffer.clone().readString(charset);
        }

        LogUtils.i(String.format("发送请求\nmethod：%s\nurl：%s\nheaders: %s \nbody：%s\n收到响应\ncode: %s %s \n时间 %s ms", request.method(), request.url(), request.headers(), body, response.code(), response.message(), tookMs));
        //json类型的响应数据，一些手机上显示不出来，不知道什么鸡毛问题，手头的手机显示不出来就把rBody放到上一行显示String类型去
        LogUtils.json(LogUtils.I,rBody);

        return response;
    }
}
