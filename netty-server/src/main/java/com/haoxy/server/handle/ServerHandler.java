package com.haoxy.server.handle;

import com.alibaba.fastjson.JSONArray;
import com.haoxy.common.cmd.CmdException;
import com.haoxy.common.cmd.DefaultFunctionsTable;
import com.haoxy.common.cmd.FunctionsTable;
import com.haoxy.common.executor.Cmd;
import com.haoxy.common.executor.CmdDefault;
import com.haoxy.common.message.Message;
import com.haoxy.common.message.Receive;
import com.haoxy.common.model.CustomProtocol;
import com.haoxy.server.util.NettySocketHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by haoxy on 2018/10/17.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
public class ServerHandler<Result> extends SimpleChannelInboundHandler {
    Cmd cmd;




    public ServerHandler() throws CmdException {
        cmd = new CmdDefault("com.haoxy.server.controller");
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);
    private static final ByteBuf HEART_BEAT = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(new CustomProtocol(123456L, "pong").toString(), CharsetUtil.UTF_8));

    /**
     * 取消绑定
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettySocketHolder.remove((NioSocketChannel) ctx.channel());
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object ob) throws Exception {
        Receive receive = (Receive) ob;
        Result result = (Result) cmd.exe(receive);
    }





    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                LOGGER.info("已经5秒没有收到信息！");
                //向客户端发送消息
                ctx.writeAndFlush(HEART_BEAT).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        }
        super.userEventTriggered(ctx, evt);
    }




}
