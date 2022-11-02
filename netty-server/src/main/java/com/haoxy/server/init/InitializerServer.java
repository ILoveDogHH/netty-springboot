package com.haoxy.server.init;

import com.haoxy.common.code.MyDecoder;
import com.haoxy.common.code.MyEncoder;

import com.haoxy.common.message.SentMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;


public class InitializerServer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                .addLast(new MyEncoder(SentMessage.class))
                .addLast(new MyDecoder(SentMessage.class))
                //10 秒没发送消息 将IdleStateHandler 添加到 ChannelPipeline 中
                //.addLast(new IdleStateHandler(0, 0, 0))
                .addLast(new MyServerHandler());
    }
}
