package com.haoxy.client.init;

import com.haoxy.common.proxy.RpcRequest;
import com.haoxy.common.proxy.RpcResponse;

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


        return this.request(rpcRequest);
    }


    private Object request(RpcRequest rpcRequest) throws ClassNotFoundException {
        //获取需要请求的地址
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 11211);

        Object result;
        RpcResponse rpcResponse = (RpcResponse) MyNettyClient.send(rpcRequest,address);
        result = rpcResponse.getResult();
        return result;
    }
}
