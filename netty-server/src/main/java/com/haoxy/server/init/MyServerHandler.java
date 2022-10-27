package com.haoxy.server.init;

import com.haoxy.common.proxy.RpcRequest;
import com.haoxy.common.proxy.RpcResponse;
import com.haoxy.common.utils.SpringBeanFactory;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yuyufeng
 * @date 2017/8/28
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest rpcRequest = (RpcRequest) msg;
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
