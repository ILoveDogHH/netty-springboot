package com.haoxy.server.service;

import com.haoxy.server.dao.HeyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeyService {


    @Autowired
    HeyDao heyDao;

    public String getHey(){
        return heyDao.getHey();
    }
}
