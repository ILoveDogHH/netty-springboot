package com.haoxy.common.request;

import com.jedigames.transport.message.base.ReceivedMessage;

public interface CallbackOnResponse {
	/**
	 * 多少毫秒之后执行onTimeout. </br>
	 * -1为一直等到response
	 * 
	 * @return
	 */
	default long getTimeoutMills() {
		return -1L;
	}
	/**
	 * 消息处理
	 * 
	 * @param message
	 * @throws Exception
	 */
	void onResponse(ReceivedMessage<?> message) throws Exception;

	/**
	 * 超时处理
	 */
	void onTimeout();
}
