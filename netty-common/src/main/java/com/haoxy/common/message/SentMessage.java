package com.haoxy.common.request;

import com.haoxy.common.message.Message;
import com.haoxy.common.message.MessageAbstract;
import com.haoxy.common.proxy.RpcRequest;

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
