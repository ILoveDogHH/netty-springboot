package com.haoxy.server.init;

import com.haoxy.common.code.MyDecoder;
import com.haoxy.common.code.MyEncoder;
import com.haoxy.common.proxy.MyServerHandler;
import com.haoxy.common.proxy.ProxyHandler;
import com.haoxy.common.proxy.RpcRequest;
import com.haoxy.server.encode.ServerDecoder;
import com.haoxy.server.handle.HeartBeatSimpleHandle;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by haoxy on 2018/10/17.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
public class HeartbeatInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                //五秒没有收到消息 将IdleStateHandler 添加到 ChannelPipeline 中
                //.addLast(new IdleStateHandler(5, 0, 0))
                .addLast(new MyEncoder(RpcRequest.class))
                .addLast(new MyDecoder(RpcRequest.class))
                .addLast(new MyServerHandler());
    }
}
