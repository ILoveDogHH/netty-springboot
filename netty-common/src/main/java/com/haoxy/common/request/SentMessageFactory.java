package com.jedigames.transport.message.base;

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
	SentMessage<T> create(int uid, int id, int opcode, T data);
}
