package com.haoxy.server.init;

import com.haoxy.common.handler.HandlerExecutor;
import com.haoxy.common.handler.MessageHandler;
import com.haoxy.common.message.RpcRequest;
import com.haoxy.common.message.SentMessage;
import com.haoxy.common.request.RequestFactory;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;


public class MyServerHandler extends MessageHandler {




    public MyServerHandler(HandlerExecutor handlerExecutor, RequestFactory factory) {
        super(handlerExecutor, factory);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	SentMessage message = (SentMessage) msg;
    	handleReceivedMessage(ctx, message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
