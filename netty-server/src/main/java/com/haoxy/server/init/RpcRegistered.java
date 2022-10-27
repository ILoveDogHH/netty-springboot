package com.haoxy.server.init;

import java.util.concurrent.ConcurrentHashMap;

public class RpcRegistered {

    /**
     * 暴露接口的实现类存放容器
     */
    private static ConcurrentHashMap<String, Class<?>> registerServices = new ConcurrentHashMap<>();


    public static void register(String className, Class<?> aclass){
        registerServices.put(className, aclass);
    }

    public static Class<?> getClass(String className){
        return registerServices.get(className);
    }

}
