package com.queerlab.chat.http.error;
/**
 * @ProjectName: QueerlabChat
 * @Package: com.queerlab.chat.http.error
 * @ClassName: ErrorHandlerFactory
 * @Description: 自定义异常类型
 * @Author: 鹿鸿祥
 * @CreateDate: 2020/8/31 09:57
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/8/31 09:57
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ErrorHandlerFactory {
    private ResponseErrorListener mResponseErrorListener;

    public ErrorHandlerFactory(ResponseErrorListener mResponseErrorListener) {
        this.mResponseErrorListener = mResponseErrorListener;
    }

    /**
     *  处理错误
     * @param throwable
     */
    public void handleError(Throwable throwable) {
        mResponseErrorListener.handlerResponseError(throwable);
    }
}
