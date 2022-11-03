package com.haoxy.common.request;

import com.haoxy.common.message.Result;

public interface SubRequestSuccess<T> {
    void success(Result<T> data);
}
