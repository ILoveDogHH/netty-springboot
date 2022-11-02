package com.haoxy.common.message;

public class RequestResult<T> implements Result {

    public static final int SUCCESS = 0;
    public static final int CODE_ERROR = -1;

    private int code;

    private T result;

    private RequestResult(int code, T result){
        this.code = code;
        this.result = result;
    }


    /**
     * 发生错误
     *
     * @param error
     * @return
     */
    public static RequestResult<Throwable> error(Throwable error) {
        return new RequestResult<>(CODE_ERROR, error);
    }


    /**
     * 成功请求
     *
     * @return
     */
    public static <T> RequestResult<T> success(T data) {
        return new RequestResult<>(SUCCESS, data);
    }




    @Override
    public int getCode() {
        return code;
    }

    @Override
    public Object getResult() {
        return result;
    }
}
