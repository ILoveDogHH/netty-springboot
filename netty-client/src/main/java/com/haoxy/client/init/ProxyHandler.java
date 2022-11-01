package com.haoxy.client.init;

import com.haoxy.common.message.MessageAbstract;
import com.haoxy.common.opcode.Opcode;
import com.haoxy.common.proxy.RpcRequest;
import com.haoxy.common.proxy.RpcResponse;
import com.haoxy.common.request.CallbackOnResponse;
import com.haoxy.common.request.SubRequestSuccess;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

public class ProxyHandler implements InvocationHandler {


    public Class<?> service;

    public ProxyHandler(Class<?> service){
        this.service = service;
    }


    @Override
    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
        //准备传输的对象
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName(service.getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setArguments(args);
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setReturnType(method.getReturnType());
        final Object[] result = {};
        MyNettyClient.newRequest(Opcode.RPC_REQUEST, rpcRequest, new SubRequestSuccess() {
            @Override
            public void success(Object data) {
                result[0] = data;
            }
        });
        return result[0];
    }

}
