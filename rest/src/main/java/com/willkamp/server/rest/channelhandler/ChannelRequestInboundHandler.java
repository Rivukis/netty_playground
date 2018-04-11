package com.willkamp.server.rest.channelhandler;

import com.google.common.collect.ImmutableMap;
import com.willkamp.server.rest.factory.ResponseBuilder;
import com.willkamp.server.rest.model.Pojo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import static io.netty.buffer.Unpooled.copiedBuffer;

public class ChannelRequestInboundHandler extends ChannelInboundHandlerAdapter {

    private final ImmutableMap<String, RequestHandler> handlers;

    ChannelRequestInboundHandler(ImmutableMap<String, RequestHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String path = request.uri();
            RequestHandler handler = handlers.get(path);
            if (handler != null) {
                ResponseBuilder responseBuilder = new ResponseBuilder(ctx.alloc());
                FullHttpResponse response = handler.request(responseBuilder).buildH1();
                ctx.writeAndFlush(response);
                return;
            } else {
                ctx.writeAndFlush(new ResponseBuilder(ctx.alloc())
                        .setStatus(HttpResponseStatus.NOT_FOUND)
                        .setBody(new Pojo("ruh roh", "not found!"))
                        .buildH1());
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
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.INTERNAL_SERVER_ERROR, data);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, data.readableBytes());
        ctx.writeAndFlush(response);
    }
}
