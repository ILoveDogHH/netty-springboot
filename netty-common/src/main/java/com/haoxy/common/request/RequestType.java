package com.jedigames.transport.message.request;

public enum RequestType {
	/**
	 * 同步请求, 用于阻塞型请求
	 */
	SYNC,
	/**
	 * 异步请求, 用于非阻塞型请求
	 */
	ASYNC
}
