package com.haoxy.common.message;

import com.alibaba.fastjson.JSONArray;

public class ResultArray {
	
	
	private final static int SUCCESS = 0;
	
	private final static int ERROR = 1;
	
	
	
	public static JSONArray success(Object... args) {
		JSONArray array = new JSONArray();
		array.add(SUCCESS);
		for(int size = 0 ; size < args.length ; size ++) {
			array.add(args[size]);
		}
		return array;
	}
	
	
	public static JSONArray error(Object... args) {
		JSONArray array = new JSONArray();
		array.add(ERROR);
		for(int size = 0 ; size < args.length ; size ++) {
			array.add(args[size]);
		}
		return array;
	}

}
