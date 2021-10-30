package com.queerlab.chat.http.conver;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.http.conver
 * @ClassName: AppResponseBodyConverter
 * @Description: 自定义的Gson解析工厂
 * @Author: 鹿鸿祥
 * @CreateDate: 2020/8/31 09:57
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/8/31 09:57
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class AppResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private Gson gson;
    private Type type;

    AppResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        String response = value.string();
        try {
//            LogUtils.e(response);
//            LogUtils.json(LogUtils.I,response);

            JSONObject jsonObj = new JSONObject(response);
            int code = jsonObj.getInt("code");
            String data = jsonObj.getString("data");

            //若为String类型数据时 拦截数据返回
            if (type.toString().equals("class java.lang.String")){
                return (T)response;
            }

            //数据不为空时 返回实体类数据
            if (data!=null && !data.equals("[]") && !data.equals("") && !data.equals("{}")) { //成功
                return GsonUtils.fromJson(response, type);
            }else {
                String str = "class";
                try {
                    //先判断当前拦截数据是否包含实体类bean数据
                    if (type.toString().contains("<com.queerlab.chat.bean")){
                        //不包含 数据为空时 截取范型中的实体类 返回string数据
                        String str2=type.toString().substring(0, type.toString().indexOf("<"));
                        Class classs =  Class.forName(str2);
                        return GsonUtils.fromJson(response, (Type) classs);
                    }else {
                        //若不包含实体类bean数据时 直接返回数据
                        Class classs =  Class.forName(type.toString().substring(str.length()).trim());
                        return GsonUtils.fromJson(response, (Type) classs);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            value.close();
        }
        return null;
    }
}
