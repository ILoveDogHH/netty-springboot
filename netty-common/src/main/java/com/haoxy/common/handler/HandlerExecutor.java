package com.haoxy.common.handler;

import com.haoxy.common.message.Message;
import com.haoxy.common.message.Result;

public interface HandlerExe {
    Result<?> exe(Message message);
}
