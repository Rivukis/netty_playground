package com.willkamp.server.rest;

import com.google.common.collect.ImmutableMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;

import static io.netty.buffer.Unpooled.copiedBuffer;

public class ChannelRequestInboundHandler extends ChannelInboundHandlerAdapter {

    private final static AttributeKey<HttpVersion> VERSION_KEY = AttributeKey.valueOf("http_version");

    private final ImmutableMap<String, RequestHandler> handlers;

    ChannelRequestInboundHandler(ImmutableMap<String, RequestHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpMessage) {
            ctx.channel().attr(VERSION_KEY).setIfAbsent(((HttpMessage) msg).protocolVersion());
        }
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String path = request.uri();
            RequestHandler handler = handlers.get(path);
            if (handler != null) {
                ResponseBuilder responseBuilder = new ResponseBuilder(ctx.alloc())
                        .setVersion(request.protocolVersion());
                FullHttpResponse response = handler.request(responseBuilder).build();
                ctx.writeAndFlush(response);
                return;
            } else {
                ctx.writeAndFlush(new ResponseBuilder(ctx.alloc())
                        .setStatus(HttpResponseStatus.NOT_FOUND)
                        .setBody(new Pojo("ruh roh", "not found!"))
                        .setVersion(request.protocolVersion()).build());
                return;
            }
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String message = cause.getMessage();
        final ByteBuf data;
        if (message == null) {
            data = Unpooled.EMPTY_BUFFER;
        } else {
            data = copiedBuffer(message.getBytes());
        }
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                ctx.channel().attr(VERSION_KEY).get(), HttpResponseStatus.INTERNAL_SERVER_ERROR, data);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, data.readableBytes());
        ctx.writeAndFlush(response);
    }
}
