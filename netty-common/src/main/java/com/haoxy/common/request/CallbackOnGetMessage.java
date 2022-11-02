package com.haoxy.common.request;

import com.haoxy.common.message.Message;
import com.haoxy.common.message.MessageAbstract;

public interface CallbackOnGetMessage {
	void callback(Message<?> message);
}
