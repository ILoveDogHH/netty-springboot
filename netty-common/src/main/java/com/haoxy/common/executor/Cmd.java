package com.haoxy.common.executor;

import com.haoxy.common.message.Receive;

public interface Cmd<T> {
   T exe(Receive receive);
}
