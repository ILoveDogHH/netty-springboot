package com.haoxy.client;

import com.haoxy.client.init.RemoteService;
import com.haoxy.server.controller.HelloService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by haoxy on 2018/10/17.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
@SpringBootApplication
public class ClientApp {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ClientApp.class,args);


        //获取动态代理的HelloService的“真实对象（其实内部不是真实的，被换成了调用远程方法）”
        HelloService helloService = RemoteService.newRemoteProxyObject(HelloService.class);
        String result = helloService.sayHello("yyf");
        System.out.println(result);
    }
}
