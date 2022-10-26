package com.haoxy.common.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

public class ProxyHandler implements InvocationHandler {



    @Override
    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
        //准备传输的对象
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName(object.getClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setArguments(args);
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setReturnType(method.getReturnType());
        return this.request(rpcRequest);
    }

    private Object request(RpcRequest rpcRequest) throws ClassNotFoundException {
//        //获取需要请求的地址
//        remoteAddress = ClientCluster.getServerIPByRandom(rpcRequest.getServiceName());
//        if (remoteAddress == null) {
//            return null;
//        }
//        Object result;
//        RpcResponse rpcResponse = (RpcResponse) MyNettyClient.send(rpcRequest,remoteAddress);
//        result = rpcResponse.getResult();
        return "";
    }

}
