package com.haoxy.common.request;

import com.haoxy.common.message.Message;
import com.haoxy.common.message.Result;
import io.netty.channel.Channel;


public class RequestCallback<T> implements CallbackOnResponse<T>{
    private SubRequestSuccess callback;
    private Channel channel;
    private volatile boolean endPause = false;
    private final static int MAX_WAIT_MILLIS = 30000;

    public RequestCallback(Channel channel, SubRequestSuccess callback) {
        this.callback = callback;
        this.channel = channel;
    }

    public void resumeThread() {
        endPause = true;
        if(channel != null){
            synchronized (channel) {
                channel.notifyAll();
            }
        }
    }

    public void pauseThread() {
        if (endPause) {
            return;
        }
        if(channel != null){
            synchronized (channel) {
                try {
                    channel.wait(MAX_WAIT_MILLIS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    @Override
    public void onResponse(Message<Result> message) throws Exception {
        try {
            callback.success(message.getData());
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            resumeThread();
        }
    }

    @Override
    public long getTimeoutMills() {
        return MAX_WAIT_MILLIS;
    }



    @Override
    public void onTimeout() {
        //超时间了直接关闭该session把--
        channel.closeFuture();
    }



}
