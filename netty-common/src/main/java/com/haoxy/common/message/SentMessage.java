package com.haoxy.common.message;

public class SentMessage<T> extends MessageAbstract<T> implements Message {


	public SentMessage(){

	}


	public SentMessage(int id, int opcode, T data) {
		super(id, opcode, 0, data);
	}



	public T getData() {
		return data;
	}


	
}
