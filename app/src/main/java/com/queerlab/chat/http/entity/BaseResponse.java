package com.queerlab.chat.http.entity;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.http.entity
 * @ClassName: BaseResponse
 * @Description: data为object
 * @Author: 鹿鸿祥
 * @CreateDate: 2020/8/31 18:07
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/8/31 18:07
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class BaseResponse<T> {

    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
