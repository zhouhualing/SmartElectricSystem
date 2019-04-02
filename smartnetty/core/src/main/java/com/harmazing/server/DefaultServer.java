package com.harmazing.server;

import com.harmazing.Config;
import com.harmazing.cache.ActiveCommandQueue;
import com.harmazing.cache.OpenNetworkCache;
import com.harmazing.protobuf.MessageProtos;
import com.harmazing.service.DeviceService;
import com.harmazing.service.impl.DeviceServiceImpl;

import io.netty.bootstrap.ChannelFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DefaultServer extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultServer.class);
    private final static Config CONFIG = Config.getInstance();
    
    ActiveCommandQueue activeQueue = null;
    OpenNetworkCache openNetworkCache = null;
    
    
    public DefaultServer(ActiveCommandQueue queue, OpenNetworkCache cache ){
    	activeQueue = queue;
    	openNetworkCache = cache;
    }

    		
    @Override
    public void run() {
        LOGGER.info("Start Socket Server....");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(32);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // Decoders
                            pipeline.addLast("frameDecoder", new DefaultProtobufVarint32FrameDecoder());
                            pipeline.addLast("protobufDecoder", new ProtobufDecoder(MessageProtos.SpmMessage.getDefaultInstance()));
                            // Encoder
                            pipeline.addLast("frameEncoder", new DefaultProtobufVarint32LengthFieldPrepender());
                            // pipeline.addLast("protobufEncoder", new ProtobufEncoder());
                            pipeline.addLast("protobufEncoder", new DefaultProtobufEncoder());
                            pipeline.addLast(new IdleStateHandler(120, 0, 0));
                            pipeline.addLast(new AuthenticateHandler());
                            pipeline.addLast(new DefaultServerHandler(activeQueue, openNetworkCache));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(CONFIG.SERVER_PORT).sync();

            // Wait until the server socket is closed.
            // In this application, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error(e.toString());
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
        LOGGER.info("Server started succesfully.");
    }
}
