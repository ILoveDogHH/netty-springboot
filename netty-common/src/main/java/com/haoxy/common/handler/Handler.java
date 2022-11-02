package com.haoxy.common.handler;

import com.haoxy.common.message.Message;
import com.haoxy.common.message.ReceivedMessageType;
import com.haoxy.common.message.Result;
import io.netty.channel.ChannelHandlerContext;

public interface Handler<T> {

    ReceivedMessageType getReceivedMessageType(int opcode);

    void handleReceivedMessage(ChannelHandlerContext handlerContext, Message<T> message);

    void handleRequest(ChannelHandlerContext handlerContext, Message<T> message);

    void handleResponse(ChannelHandlerContext handlerContext, Message<T> message);

    void handleUnknownMessage(ChannelHandlerContext handlerContext, Message<T> message);

    void handleResult(ChannelHandlerContext handlerContext, Message<T> message, Result<?> result);
}
