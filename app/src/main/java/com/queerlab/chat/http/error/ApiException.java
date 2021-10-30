package com.queerlab.chat.http.error;

/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.http.error
 * @ClassName: ApiException
 * @Description: 自定义异常类型
 * @Author: 鹿鸿祥
 * @CreateDate: 2020/8/31 09:57
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/8/31 09:57
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ApiException extends RuntimeException {

    private int errorCode;
    private String msg;

    public ApiException(int errorCode, String msg) {
        this.msg = msg;
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }
}
