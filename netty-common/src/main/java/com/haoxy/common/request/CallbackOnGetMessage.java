package com.haoxy.common.request;

import com.haoxy.common.message.MessageAbstract;

public interface CallbackOnGetMessage<T extends  MessageAbstract> {
	void callback(T message);
}
