package com.haoxy.server.init;

import com.haoxy.common.message.RpcRequest;
import com.haoxy.common.message.SentMessage;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;


public class MyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	SentMessage message = (SentMessage) msg;
    	int opcode = message.getOpcode();


    	RpcRequest rpcRequest = (RpcRequest) message.getData();
        Class clazz = RpcRegistered.getClass(rpcRequest.getServiceName());
        if (clazz == null) {
            throw new Exception("没有找到类 " + rpcRequest.getServiceName());
        }
        Method method = clazz.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
        if (method == null) {
            throw new Exception("没有找到相应方法 " + rpcRequest.getMethodName());
        }
        //执行真正要调用的方法
        Object result = method.invoke(clazz.newInstance(), rpcRequest.getArguments());
        //返回执行结果给客户端
        RpcResponse rpcResponse = new RpcResponse(result);
        ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
