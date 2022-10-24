package com.haoxy.common.cmd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdReturn {
	public static final int ERROR = 0;//客户端请求失败
	public static final int SUCCESS = 1;//客户端请求成功
	public static final int BAN_ERROR = 101; //被封禁
	
	/**
	 * 异步请求的返回值, 用于在MessageExecutor执行之后,不向玩家写数据
	 */
	public static final CmdReturn ASYNC_RETURN = new CmdReturn(new JSONArray().fluentAdd("async-return"));

	private JSONArray ret;
	
	public CmdReturn(JSONArray ret) {
		this.ret=ret;
	}
	
	public CmdReturn(List<Object> msg) {
		ret=new JSONArray(msg);
	}

	/**
	 * 把一个元素加到最后面
	 * @param e
	 * @return
	 */
	public CmdReturn add(Object e) {
		ret.add(e);
		return this;
	}
	
	public Object get(int index) {
		return ret.get(index);
	}
	
	public <T> T getObject(int index, Class<T> claz) {
		return ret.getObject(index, claz);
	}
	
	public int getCode() {
		return ret.getObject(0, int.class);
	}
	
	public JSONArray toJSONArray() {
		return ret;
	}
	
	@Override
	public String toString() {
		// 返回ret.toString
		return JSON.toJSONString(ret, SerializerFeature.WriteNonStringKeyAsString);
	}
	
	/**
	 * 其他
	 * @param infoCode
	 * @param msg
	 * @return
	 */
	public static CmdReturn info(int infoCode, Object... msg) {
		Object[] msgNew=new Object[msg.length+1];
		msgNew[0]=infoCode;
		System.arraycopy(msg, 0, msgNew, 1, msg.length);
		return new CmdReturn(new ArrayList<>(Arrays.asList(msgNew)));
	}
	
	/**
	 * 成功
	 * @param msg
	 * @return
	 */
	public static CmdReturn success(Object... msg) {
		return info(SUCCESS, msg);
	}
	
	/**
	 * 失败
	 * @param msg
	 * @return
	 */
	public static CmdReturn error(Object... msg) {
		return info(ERROR, msg);
	}

	public static CmdReturn banError(Object... msg) {
		return info(BAN_ERROR, msg);
	}
	


}
