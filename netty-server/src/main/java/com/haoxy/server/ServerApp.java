package com.haoxy.server;

import com.haoxy.common.utils.SpringBeanFactory;
import com.haoxy.server.controller.HelloService;
import com.haoxy.server.controller.HelloServiceImp;
import com.haoxy.server.init.RpcRegistered;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

/**
 * Created by haoxy on 2018/10/17.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
@SpringBootApplication
public class ServerApp {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(ServerApp.class)
                .web(false)
                .run(args);
        RpcRegistered.register(HelloService.class.getName(), HelloServiceImp.class);
        new SpringBeanFactory(applicationContext);
    }
}
