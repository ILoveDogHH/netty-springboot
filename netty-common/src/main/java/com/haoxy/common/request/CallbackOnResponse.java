package com.haoxy.common.request;

import com.haoxy.common.message.MessageAbstract;

public interface CallbackOnResponse {
	/**
	 * 多少毫秒之后执行onTimeout. </br>
	 * -1为一直等到response
	 * 
	 * @return
	 */
	long getTimeoutMills();
	/**
	 * 消息处理
	 * 
	 * @param message
	 * @throws Exception
	 */
	void onResponse(MessageAbstract<?> message) throws Exception;

	/**
	 * 超时处理
	 */
	void onTimeout();
}
