package com.haoxy.common.message;

import com.alibaba.fastjson.JSONArray;

public class ReceiveParams {
    String function;
    JSONArray params;

    public ReceiveParams(String function, JSONArray params){
        this.function = function;
        this.params = params;
    }

    public String getFunction(){
        return function;
    }

    public JSONArray getParams(){
        return params;
    }
}
