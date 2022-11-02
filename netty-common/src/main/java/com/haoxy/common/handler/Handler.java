package com.haoxy.common.handler;

import com.haoxy.common.message.Message;
import com.haoxy.common.message.ReceivedMessageType;
import io.netty.channel.ChannelHandlerContext;

public interface Handler<T> {

    ReceivedMessageType getReceivedMessageType(int opcode);

    void handleRequest(ChannelHandlerContext handlerContext, Message<T> message);

    void handleResponse(ChannelHandlerContext handlerContext, Message<T> message);

    void handleUnknownMessage(ChannelHandlerContext handlerContext, Message<T> message);
}
