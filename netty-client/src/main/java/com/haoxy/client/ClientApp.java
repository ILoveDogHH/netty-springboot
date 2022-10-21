package com.haoxy.client;

import com.haoxy.client.controller.Login;
import com.haoxy.client.util.SpringBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by haoxy on 2018/10/17.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
@SpringBootApplication
public class ClientApp {
    public static ApplicationContext applicationContext = null;


    public static void main(String[] args) {
        applicationContext = SpringApplication.run(ClientApp.class,args);
        SpringBeanFactory factory =  new SpringBeanFactory();
        factory.setApplicationContext(applicationContext);
        Object login = factory.getBean("getLogin");
        System.out.println(login);

        Object login2 = factory.getBean("getLogin");
        System.out.println(login2);
    }
}
