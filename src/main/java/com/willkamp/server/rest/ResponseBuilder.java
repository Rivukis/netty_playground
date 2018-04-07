package com.willkamp.server.rest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class ResponseBuilder {

    private HttpVersion version;
    private HttpResponseStatus status;
    private String body = null;
    private Map<String, Object> headers = null;

    public ResponseBuilder setVersion(HttpVersion version) {
        this.version = version;
        return this;
    }

    public ResponseBuilder setStatus(HttpResponseStatus status) {
        this.status = status;
        return this;
    }

    public ResponseBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public ResponseBuilder addHeader(String key, Object value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
        return this;
    }

    public FullHttpResponse build() {
        checkNotNull(version, "HttpVersion not set");
        checkNotNull(status, "HttpResponseStatus not set");
        final ByteBuf data;
        final int length;
        if (body != null) {
            data = Unpooled.copiedBuffer(body.getBytes(StandardCharsets.UTF_8));
            length = body.length();
        } else {
            data = Unpooled.EMPTY_BUFFER;
            length = 0;
        }
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(version, status, data);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, length);
        return response;
    }
}
