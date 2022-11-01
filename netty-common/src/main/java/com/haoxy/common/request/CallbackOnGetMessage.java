package com.haoxy.common.request;

public interface CallbackOnGetMessage<T> {
	void callback(SentMessage<T> message);
}
