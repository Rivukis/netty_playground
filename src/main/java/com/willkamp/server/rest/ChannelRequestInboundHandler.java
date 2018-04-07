package com.willkamp.server.rest;

import com.google.common.collect.ImmutableMap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

public class ChannelRequestInboundHandler extends ChannelInboundHandlerAdapter {

    private final ImmutableMap<String, RequestHandler> handlers;

    ChannelRequestInboundHandler(ImmutableMap<String, RequestHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String path = request.uri();
            RequestHandler handler = handlers.get(path);
            if (handler != null) {
                FullHttpResponse response = handler.request(request).build();
                ctx.writeAndFlush(response);
                return;
            } else {
                DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                        request.protocolVersion(),
                        HttpResponseStatus.NOT_FOUND,
                        Unpooled.EMPTY_BUFFER);
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
                ctx.writeAndFlush(response);
                return;
            }
        }
        ctx.fireChannelRead(msg);
    }
}
