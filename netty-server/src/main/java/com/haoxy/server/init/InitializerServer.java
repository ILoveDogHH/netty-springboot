package com.haoxy.server.init;

import com.haoxy.common.code.ServerDecoder;
import com.haoxy.common.code.ServerEncode;
import com.haoxy.server.handle.ServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by haoxy on 2018/10/17.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
public class InitializerServer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                .addLast(new ServerEncode())
                .addLast(new ServerDecoder())
                //五秒没有收到消息 将IdleStateHandler 添加到 ChannelPipeline 中
               // .addLast(new IdleStateHandler(0, 0, 0))
                .addLast(new ServerHandler());
    }
}
