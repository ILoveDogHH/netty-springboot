package com.haoxy.common.handler;

import com.haoxy.common.message.Message;
import com.haoxy.common.message.Result;
import io.netty.channel.ChannelHandlerContext;

public interface HandlerExecutor {
    Result<?> exe(Message message) throws Exception;

    void handResult(ChannelHandlerContext handlerContext, Message message, Result result);
}
