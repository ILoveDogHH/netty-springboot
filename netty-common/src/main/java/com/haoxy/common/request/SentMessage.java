package com.jedigames.transport.message.base;

public abstract class SentMessage<T> extends AbstractMessage<T> implements MessageSent {
	//为网关调用 
	private long sessionId;
	private boolean error;//这个仅仅为二进制 发送错误String准备
	public SentMessage(int uid, int id, int opcode, T data) {
		super(id, opcode);
		this.data = data;
	}

	@Override
	public T getData() {
		return data;
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean isError) {
		this.error = isError;
	}
	
}
