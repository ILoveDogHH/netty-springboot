package com.haoxy.client.init;

import com.haoxy.common.handler.HandlerExecutor;
import com.haoxy.common.handler.HandlerExecutorImp;
import com.haoxy.common.handler.MessageHandler;
import com.haoxy.common.request.AbstractRequestFactory;
import com.haoxy.common.request.RequestFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *  Netty适配器
 * @author yuyufeng
 * @date 2017/8/28
 */
public class MyClientHandler extends MessageHandler {

    private Object result;

    public MyClientHandler(HandlerExecutor handlerExecutor, RequestFactory factory) {
        super(handlerExecutor, factory);
    }


    public Object getResult() {
        return result;
    }
    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//        System.out.println("MyClientHandler.channelActive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        System.out.println("read Message:"+msg);
        result = msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
