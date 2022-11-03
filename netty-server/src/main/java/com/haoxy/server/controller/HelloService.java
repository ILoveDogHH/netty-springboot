package com.haoxy.server.controller;

import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;

public interface HelloService {

    JSONArray sayHello(String words) throws InterruptedException;
}
