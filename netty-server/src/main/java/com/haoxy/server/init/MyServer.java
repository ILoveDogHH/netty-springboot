package com.haoxy.server.init;

import com.haoxy.common.code.MyDecoder;
import com.haoxy.common.code.MyEncoder;
import com.haoxy.common.handler.MessageHandler;
import com.haoxy.common.message.SentMessage;
import com.haoxy.common.request.AbstractRequestFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;


@Component
public class MyServer {





    private final static Logger LOGGER = LoggerFactory.getLogger(MyServer.class);
    
    /**
     * NioEventLoopGroup是一个处理I / O操作的多线程事件循环。 Netty为不同类型的传输提供各种EventLoopGroup实现。
     * 我们在此示例中实现了服务器端应用程序，因此将使用两个NioEventLoopGroup。第一个，通常称为“老板”，接受传入连接。第二个，通常称为“工人”，
     * 一旦老板接受连接并将接受的连接注册到工作人员，就处理被接受连接的流量。使用了多少个线程以及它们如何映射到创建的Channels取决于EventLoopGroup实现，甚至可以通过构造函数进行配置。
     */
    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup work = new NioEventLoopGroup();

    @Value("${netty.server.port}")
    private int nettyPort;

    /**
     * 启动 Netty
     *
     * @return
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(nettyPort))
                //保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new MyEncoder(SentMessage.class))
                                .addLast(new MyDecoder(SentMessage.class))
                                //10 秒没发送消息 将IdleStateHandler 添加到 ChannelPipeline 中
                                .addLast(new IdleStateHandler(5, 5, 20, TimeUnit.SECONDS))
                                .addLast(new MyServerHandler(new ServerHandlerExecutor(), new AbstractRequestFactory() {}));
                    }
                });
        //绑定并开始接受传入的连接。
        ChannelFuture future = bootstrap.bind().sync();
        if (future.isSuccess()) {
            LOGGER.info("启动 Netty 成功");
        }
    }

    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        boss.shutdownGracefully().syncUninterruptibly();
        work.shutdownGracefully().syncUninterruptibly();
        LOGGER.info("关闭 Netty 成功");
    }
}
