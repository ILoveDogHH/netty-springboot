package com.haoxy.common.request;

import com.haoxy.common.message.MessageAbstract;
import io.netty.channel.ChannelFuture;

public class CallBackOnResponseImp implements CallbackOnResponse{
    private ChannelFuture future;

    public CallBackOnResponseImp(ChannelFuture future){
        this.future = future;
    }


    private static final Long OUT_TIME = 3000L;

    @Override
    public long getTimeoutMills() {
        return OUT_TIME;
    }

    @Override
    public void onResponse(MessageAbstract<?> message) throws Exception {
        System.out.println("获得了请求的数据");
    }

    @Override
    public void onTimeout() {
        //如果时间超出了则关闭连接
        future.channel().closeFuture();
    }
}
