package com.haoxy.server.dao;

import com.haoxy.server.service.HeyService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class HeyDao {



    @Cacheable(value = "HeyDao", key = "#userId", sync = true)
    public String getHey(int userId){
        System.out.println("sql");
        return "cccc";
    }

}
