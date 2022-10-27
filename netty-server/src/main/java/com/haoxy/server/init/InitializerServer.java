package com.haoxy.server.init;

import com.haoxy.common.code.MyDecoder;
import com.haoxy.common.code.MyEncoder;
import com.haoxy.common.proxy.RpcRequest;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * Created by haoxy on 2018/10/17.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
public class InitializerServer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                .addLast(new MyEncoder(RpcRequest.class))
                .addLast(new MyDecoder(RpcRequest.class))
                //10 秒没发送消息 将IdleStateHandler 添加到 ChannelPipeline 中
                //.addLast(new IdleStateHandler(0, 0, 0))
                .addLast(new MyServerHandler());
    }
}
