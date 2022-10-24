package com.haoxy.common.executor;

import com.alibaba.fastjson.JSONArray;
import com.haoxy.common.cmd.CmdException;
import com.haoxy.common.cmd.DefaultFunctionsTable;
import com.haoxy.common.cmd.FunctionsTable;

public abstract class CmdAbstract<T> implements Cmd {

    public FunctionsTable functions;

    public CmdAbstract(String... packagesName) throws CmdException {
        FunctionsTable commandFunctions = new DefaultFunctionsTable(Object.class, JSONArray.class,
                new Class<?>[] { int.class, JSONArray.class});
        commandFunctions.init(packagesName);
        this.functions = commandFunctions;
    }


}
