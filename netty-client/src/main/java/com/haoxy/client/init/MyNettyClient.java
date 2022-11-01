package com.haoxy.client.init;

import com.haoxy.common.code.MyDecoder;
import com.haoxy.common.code.MyEncoder;
import com.haoxy.common.opcode.Opcode;
import com.haoxy.common.proxy.MyClientHandler;
import com.haoxy.common.proxy.RpcRequest;
import com.haoxy.common.proxy.RpcResponse;
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

    private static ChannelFuture future;


    private static MyNettyClient instance;

    public static MyNettyClient getInstance(){
        if(instance == null){
            synchronized (MyNettyClient.class){
                instance = new MyNettyClient();
            }
        }
        return instance;
    }


    public static void init(InetSocketAddress inetSocketAddress){
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
                            ChannelPipeline p = ch.pipeline();
                            ch.pipeline().addLast(new MyEncoder(RpcRequest.class));
                            ch.pipeline().addLast(new MyDecoder(RpcResponse.class));
                            ch.pipeline().addLast(new MyClientHandler());
                        }
                    });
            future = b.connect(inetSocketAddress.getAddress(), inetSocketAddress.getPort());
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void newRequest(int opcode, T data, SubRequestSuccess subRequestSuccess) {
        AbstractRequestFactory requestHandler = new RequestHandler();
        RequestCallback response = new RequestCallback(future, subRequestSuccess);
        requestHandler.newRequest(future,RequestType.ASYNC, opcode, data, response, new CallbackOnGetMessage<RpcRequest>() {
            @Override
            public void callback(SentMessage<RpcRequest> message) {
                future.channel().writeAndFlush(message);
            }
        });
        response.pauseThread();
    }




}
