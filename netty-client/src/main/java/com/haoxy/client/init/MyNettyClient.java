package com.haoxy.client.init;

import com.haoxy.common.code.MyDecoder;
import com.haoxy.common.code.MyEncoder;
import com.haoxy.common.handler.Handler;
import com.haoxy.common.handler.HandlerExecutorImp;
import com.haoxy.common.handler.MessageHandler;
import com.haoxy.common.message.SentMessage;
import com.haoxy.common.request.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


import java.net.InetSocketAddress;


/**
 * 客户端Netty实现
 *
 * @author yuyufeng
 * @date 2017/8/28
 */
public enum  MyNettyClient{

    INSTANCE;


    private static Integer TIMEOUT = 10000;

    private static Channel channel;

    private MessageHandler handler;

    public void init(InetSocketAddress inetSocketAddress){
        EventLoopGroup group = new NioEventLoopGroup();
        handler = new MyClientHandler(new HandlerExecutorImp(), new AbstractRequestFactory() {
        });
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new MyEncoder(SentMessage.class));
                            ch.pipeline().addLast(new MyDecoder(SentMessage.class));
                            ch.pipeline().addLast(handler);
                        }
                    });
            ChannelFuture future = b.connect(inetSocketAddress.getAddress(), inetSocketAddress.getPort()).sync();
            channel = future.channel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void newRequest(int opcode, Object data, SubRequestSuccess subRequestSuccess) {
        CallbackOnResponse response = new RequestCallback(channel, subRequestSuccess);
        handler.factory.newRequest(channel,RequestType.ASYNC, opcode, data, response, new CallbackOnGetMessage<SentMessage>() {
            @Override
            public void callback(SentMessage message) {
                channel.writeAndFlush(message);
            }
        });
        response.pauseThread();
        System.out.println("输出下日志");
    }




}
