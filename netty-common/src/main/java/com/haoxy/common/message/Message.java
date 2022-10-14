package com.haoxy.common.message;

public interface Message<T> {

    int getId();

    int getOpcode();

    int getUid();

    T getObj();

}
