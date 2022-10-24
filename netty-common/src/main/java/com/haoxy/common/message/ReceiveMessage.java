package com.haoxy.common.message;

public class ReceiveMessage<ReceiveParams> extends MessageAbstract{


    public ReceiveMessage(int id, int opcode, int uid, Object data) {
        super(id, opcode, uid, data);
    }



}
