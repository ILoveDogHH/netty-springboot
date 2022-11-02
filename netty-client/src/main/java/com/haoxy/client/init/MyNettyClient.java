package com.haoxy.client.init;

import com.haoxy.common.code.MyDecoder;
import com.haoxy.common.code.MyEncoder;
import com.haoxy.common.message.MessageAbstract;
import com.haoxy.common.message.SentMessage;
import com.haoxy.common.proxy.MyClientHandler;
import com.haoxy.common.message.RpcRequest;
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
public class MyNettyClient<T>{
    private static Integer TIMEOUT = 10000;

    private static Channel channel;


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
                            ch.pipeline().addLast(new MyClientHandler());
                        }
                    });
            ChannelFuture future = b.connect(inetSocketAddress.getAddress(), inetSocketAddress.getPort()).sync();
            channel = future.channel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public static void newRequest(int opcode, Object data, SubRequestSuccess subRequestSuccess) {
        CallbackOnResponse response = new RequestCallback(channel, subRequestSuccess);
        new AbstractRequestFactory(){}.newRequest(channel,RequestType.ASYNC, opcode, data, response, new CallbackOnGetMessage<SentMessage>() {
            @Override
            public void callback(SentMessage message) {
                channel.writeAndFlush(message);
            }
        });
        response.pauseThread();
        System.out.println("输出下日志");
    }




}
