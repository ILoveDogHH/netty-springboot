package com.haoxy.common.handler;

import com.haoxy.common.message.Message;
import com.haoxy.common.message.Result;
import io.netty.channel.ChannelHandlerContext;

public class HandlerExecutorImp implements HandlerExecutor{
    @Override
    public Result<?> exe(Message message) {
        return null;
    }

    @Override
    public void handResult(ChannelHandlerContext handlerContext, Message message, Result result) {

    }
}
