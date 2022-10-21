package com.haoxy.client.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.PreDestroy;

@Controller
public class Login {

    private static int c = 0;

    @Bean(destroyMethod = "myDestory")
    public String getLogin(){
        c++;
        System.out.println("cccccccc");
        return String.valueOf(c);
    }


    /**
     * 初始化方法
     * */
    public void myInit() {
        System.out.println("book bean被创建");
    }

    /**
     * 销毁时方法
     * */

    @PreDestroy
    public void myDestory() {
        System.out.println("book bean被销毁");
    }

}
