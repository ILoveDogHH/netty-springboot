package com.haoxy.client.init;

import com.haoxy.common.code.MyDecoder;
import com.haoxy.common.code.MyEncoder;
import com.haoxy.common.handler.MessageHandler;
import com.haoxy.common.message.Message;
import com.haoxy.common.message.SentMessage;

import com.haoxy.common.request.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


import java.net.InetSocketAddress;



public enum  MyNettyClient{

    INSTANCE;


    private static Integer TIMEOUT = 1000;

    private static Channel channel;

    private RequestFactory factory = new AbstractRequestFactory() {};

    public void init(InetSocketAddress inetSocketAddress){
        EventLoopGroup group = new NioEventLoopGroup();

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
                            ch.pipeline().addLast(new MyClientHandler(new ClientHandlerExecutor(), factory));
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
        factory.newRequest(channel,RequestType.ASYNC, opcode, data, response, new CallbackOnGetMessage() {
            @Override
            public void callback(Message message) {
                channel.writeAndFlush(message);
            }
        });
        response.pauseThread();
    }




}
