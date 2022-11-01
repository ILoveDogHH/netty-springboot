package com.haoxy.common.message;

public class ReceiveMessage<T> extends MessageAbstract{


    public ReceiveMessage(int id, int opcode, int uid, T data) {
        super(id, opcode, uid, data);
    }



}
