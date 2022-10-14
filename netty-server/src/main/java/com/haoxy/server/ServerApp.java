package com.haoxy.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by haoxy on 2018/10/17.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
@SpringBootApplication
public class ServerApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ServerApp.class)
                .web(false)
                .run(args);
    }
}
