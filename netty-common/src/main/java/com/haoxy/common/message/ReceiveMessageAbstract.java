package com.haoxy.common.message;

public class ReceiveMessageAbstract<T> implements Receive{

    int uid;

    T data;

    public ReceiveMessageAbstract(int uid, T data){
        this.uid = uid;
        this.data = data;
    }


    @Override
    public int getUid() {
        return uid;
    }

    @Override
    public Object getData() {
        return data;
    }
}
