package com.haoxy.common.request;

import com.haoxy.common.message.MessageAbstract;
import io.netty.channel.ChannelFuture;

public class RequestCallback<T> implements CallbackOnResponse{
    private SubRequestSuccess callback;
    private ChannelFuture future;
    private volatile boolean endPause = false;
    String funName = "";
    private final static int MAX_WAIT_MILLIS = 3000;

    public RequestCallback(ChannelFuture future, SubRequestSuccess callback) {
        this.callback = callback;
        this.future = future;
    }

    public void resumeThread() {
        endPause = true;
        if(future != null){
            synchronized (future) {
                future.notifyAll();
            }
        }
    }

    public void pauseThread() {
        if (endPause) {
            return;
        }
        if(future != null){
            synchronized (future) {
                try {
                    future.wait(MAX_WAIT_MILLIS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    @Override
    public void onResponse(MessageAbstract<?> message) throws Exception {
        try {
            callback.success(message.getObj());
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
        future.channel().closeFuture();
    }



}
