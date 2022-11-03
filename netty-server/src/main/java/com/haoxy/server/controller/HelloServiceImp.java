package com.haoxy.server.controller;

import com.alibaba.fastjson.JSONArray;
import com.haoxy.common.message.ResultArray;

public class HelloServiceImp implements HelloService{
    @Override
    public JSONArray sayHello(String words) throws InterruptedException {
       return ResultArray.success("这是一个测试的数据");
    }
    
    


}
