package com.haoxy.server.controller;

public class HelloServiceImp implements HelloService{
    @Override
    public String sayHello(String words) throws InterruptedException {
        Thread.sleep(5000);
        return "这是第一个测试的数据";
    }


}
