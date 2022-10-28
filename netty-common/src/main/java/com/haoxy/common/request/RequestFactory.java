package com.jedigames.transport.message.request;

import com.jedigames.transport.message.base.ReceivedMessage;

public interface RequestFactory {
	/**
	 * 生成新的请求
	 * 
	 * @param creator
	 *            创建者, 用来区分对应的threadpool
	 * @param type
	 *            消息类型RequestType:</br>
	 *            SYNC类型, 会等待消息处理完毕, 才会开始下一个message的处理;</br>
	 *            ASYNC类型, 如果当前没有SYNC类型的阻塞型请求, 将会
	 * @param uid
	 *            message的uid, 用于服务器这边做处理线程的分离
	 * @param opcode
	 * @param data
	 * @param onResponse
	 *            当消息被对面处理完回调时
	 * @param onGetMessage
	 *            消息生成之后如何处理
	 */
	<T> void newRequest(Object creator, RequestType type, int uid, int opcode, T data, CallbackOnResponse onResponse,
			CallbackOnGetMessage<T> onGetMessage);
	
	/**
	 * 检测是不是这个factory的response
	 * 
	 * @param message
	 * @return
	 */
	boolean isResponse(ReceivedMessage<?> message);

	/**
	 * 执行message对应的callback
	 * 
	 * @param message
	 * @return
	 */
	boolean doCallback(ReceivedMessage<?> message);
}
