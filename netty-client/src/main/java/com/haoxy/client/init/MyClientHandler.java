package com.haoxy.client.init;

import com.haoxy.common.handler.HandlerExecutor;
import com.haoxy.common.handler.MessageHandler;
import com.haoxy.common.message.SentMessage;
import com.haoxy.common.request.RequestFactory;
import io.netty.channel.ChannelHandlerContext;

/**
 *  Netty适配器
 * @author yuyufeng
 * @date 2017/8/28
 */
public class MyClientHandler extends MessageHandler {


    public MyClientHandler(HandlerExecutor handlerExecutor, RequestFactory factory) {
        super(handlerExecutor, factory);
    }


    

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("MyClientHandler.channelActive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SentMessage message = (SentMessage) msg;
        handleReceivedMessage(ctx, message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
