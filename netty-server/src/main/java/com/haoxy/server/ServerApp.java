package com.haoxy.server;

import com.github.jesse.l2cache.spring.EnableL2Cache;
import com.haoxy.common.utils.SpringBeanFactory;
import com.haoxy.server.RpcService.HelloService;
import com.haoxy.server.RpcService.HelloServiceImp;
import com.haoxy.server.init.RpcRegistered;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@EnableL2Cache
@SpringBootApplication
public class ServerApp {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(ServerApp.class)
//                .web(WebApplicationType.NONE)
                .run(args);
        RpcRegistered.register(HelloService.class.getName(), HelloServiceImp.class);
        new SpringBeanFactory(applicationContext);
    }
}
