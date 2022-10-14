package com.haoxy.common.message;

public class MessageDefault<T> extends MessageAbstract<T>{
    public MessageDefault(int index, int opcode, int uid, T data) {
        super(index, opcode, uid, data);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getOpcode() {
        return opcode;
    }

    @Override
    public int getUid() {
        return uid;
    }

    @Override
    public T getObj() {
        return data;
    }



}
