package com.haoxy.common.code;

import com.haoxy.common.message.Message;
import com.haoxy.common.message.MessageDefault;
import com.haoxy.common.model.CustomProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by haoxy on 2018/10/17.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 * 服务端解码器
 */
public class ServerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int id = byteBuf.readInt();
        int opcode = byteBuf.readInt();
        int uid = byteBuf.readInt();
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String content = new String(bytes);
        Message receive = new MessageDefault(id, opcode, uid, content);
        list.add(receive);
    }
}
