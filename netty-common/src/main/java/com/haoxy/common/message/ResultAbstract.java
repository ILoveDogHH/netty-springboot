package com.haoxy.common.message;

public abstract class ResultAbstract<T> implements Result {
    int code;

    T data;


    public ResultAbstract(int code, T data){
        this.code = code;
        this.data = data;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public Object getData() {
        return data;
    }

}
