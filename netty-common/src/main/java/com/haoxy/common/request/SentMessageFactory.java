package com.haoxy.common.request;

import com.haoxy.common.message.MessageAbstract;
import com.haoxy.common.message.SentMessage;

public interface SentMessageFactory<T> {
	/**
	 * 创建使用Data创建对象
	 * 
	 * @param uid
	 * @param id
	 * @param opcode
	 * @param data
	 * @return
	 */
	MessageAbstract<T> create(int uid, int id, int opcode, T data);
}
