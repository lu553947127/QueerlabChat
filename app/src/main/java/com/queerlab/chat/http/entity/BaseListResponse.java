package com.queerlab.chat.http.entity;

import java.util.List;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.http.entity
 * @ClassName: BaseListResponse
 * @Description: data为List
 * @Author: 鹿鸿祥
 * @CreateDate: 2020/8/31 18:07
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/8/31 18:07
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class BaseListResponse<T> {

    private int code;
    private String msg;
    private List<T> data;

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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
