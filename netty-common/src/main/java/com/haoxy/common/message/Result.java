package com.haoxy.common.message;

public interface Result<T> {

    int getCode();

    T getData();
}
