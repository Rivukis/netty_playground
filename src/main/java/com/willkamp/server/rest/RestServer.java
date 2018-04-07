package com.willkamp.server.rest;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

class RestServer {
    private final int port;
    private static final int NUM_THREADS = 5;

    RestServer(int port) {
        this.port = port;
    }

    void start() throws InterruptedException {
        ChannelConfig config = new ChannelConfig();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(config.eventLoopGroup)
                    .channel(config.channelClass)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new RestChannelInitializer());
            ChannelFuture channelFuture = bootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            config.eventLoopGroup.shutdownGracefully().sync();
        }
    }

    private static class ChannelConfig {
        final EventLoopGroup eventLoopGroup;
        final Class<? extends ServerChannel> channelClass;

        ChannelConfig() {
            if (Epoll.isAvailable()) {
                eventLoopGroup = new EpollEventLoopGroup(NUM_THREADS);
                channelClass = EpollServerSocketChannel.class;
            } else {
                eventLoopGroup = new NioEventLoopGroup(NUM_THREADS);
                channelClass = NioServerSocketChannel.class;
            }
        }
    }
}
