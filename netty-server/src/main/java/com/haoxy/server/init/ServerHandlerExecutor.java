package com.haoxy.server.init;

import com.alibaba.fastjson.JSONArray;
import com.haoxy.common.handler.HandlerExecutor;
import com.haoxy.common.message.*;
import com.haoxy.common.opcode.Opcode;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;

public class ServerHandlerExecutor implements HandlerExecutor {
    @Override
    public Result<?> exe(Message message) throws Exception {
        Result<?> result;
        try {
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
            JSONArray arrayResult = (JSONArray) method.invoke(clazz.newInstance(), rpcRequest.getArguments());
            return RequestResult.success(arrayResult);
        }catch (Exception e){
            return RequestResult.error(e);
        }
    }

    @Override
    public void handResult(ChannelHandlerContext handlerContext, Message message, Result result) {
        int opcode = message.getOpcode();
        Message msg = null;
        switch (opcode){
            case Opcode.RPC_REQUEST:
                msg = new SentMessage(message.getId(), Opcode.PRC_RESPONSE, result);
                break;
        }
        if(msg == null){
            return;
        }
        handlerContext.writeAndFlush(msg);
    }
}
