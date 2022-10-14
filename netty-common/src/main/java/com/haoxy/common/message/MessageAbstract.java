package com.haoxy.common.message;

/**
 * encode使用 对象转化成字节流
 * @param <T>
 */
public abstract class MessageAbstract<T> implements Message{
    public int id;

    public int opcode;

    public int uid;

    public T data;

    public MessageAbstract(int id, int opcode, int uid, T data){
        this.id = id;
        this.opcode = opcode;
        this.uid = uid;
        this.data = data;
    }

}
