package com.haoxy.server.controller;

import com.haoxy.server.service.HeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class Hay {

    @Autowired
    HeyService heyService;



    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String getHey(){
        return heyService.getHey();
    }
}
