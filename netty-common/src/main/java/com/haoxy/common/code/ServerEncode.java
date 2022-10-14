package com.haoxy.common.code;

import com.haoxy.common.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by haoxy on 2018/10/17.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 * 客户端编码器
 */
public class ServerEncode extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(message.getId());
        byteBuf.writeInt(message.getOpcode());
        byteBuf.writeInt(message.getUid());
        String data = (String) message.getObj();
        byteBuf.writeBytes(data.getBytes());
    }
}
