package com.haoxy.common.executor;

import com.alibaba.fastjson.JSONArray;
import com.haoxy.common.cmd.CmdException;
import com.haoxy.common.message.Receive;
import com.haoxy.common.message.ReceiveParams;
import com.haoxy.common.message.Result;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public class CmdDefault<Result> extends CmdAbstract{

    public CmdDefault(String... packagesName) throws CmdException {
        super(packagesName);
    }


    @Override
    public Result exe(Receive receive) {
        Result result = null;
        try {
            int uid = receive.getUid();
            ReceiveParams params = (ReceiveParams) receive.getData();
            Method method = functions.getMethodByName(params.getFunction());
            result = (Result) method.invoke(uid, params.getParams());
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
