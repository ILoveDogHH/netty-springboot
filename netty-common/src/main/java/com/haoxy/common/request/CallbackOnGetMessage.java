package com.jedigames.transport.message.request;

import com.jedigames.transport.message.base.SentMessage;

public interface CallbackOnGetMessage<T> {
	void callback(SentMessage<T> message);
}
