package com.haoxy.common.cmd;

import java.lang.reflect.Method;

public interface FunctionsTable {
	/**
	 * 针对指定的包体做初始化
	 * 
	 * @param packageNames
	 * @throws CmdException
	 */
	void init(String[] packageNames) throws CmdException;

	/**
	 * 获取方法
	 * 
	 * @param funcName
	 * @return
	 * @throws CmdException
	 */
	Method getMethodByName(String funcName) throws CmdException;
}
