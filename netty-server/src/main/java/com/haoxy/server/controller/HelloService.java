package com.haoxy.server.controller;

import org.springframework.stereotype.Controller;

@Controller
public interface HelloService {

    String sayHello(String words) throws InterruptedException;
}
