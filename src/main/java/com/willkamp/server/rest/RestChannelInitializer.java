package com.willkamp.server.rest;

import com.google.common.collect.ImmutableMap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;

public class RestChannelInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast("server codec duplex", new HttpServerCodec())
                .addLast("message size limit aggregator", new HttpObjectAggregator(512 * 1024))
                .addLast(new ChannelRequestInboundHandler(new AppHandlers().handlers()));
    }
}
