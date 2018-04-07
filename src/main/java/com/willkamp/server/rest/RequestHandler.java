package com.willkamp.server.rest;

import io.netty.handler.codec.http.FullHttpRequest;

public interface RequestHandler {
    ResponseBuilder request(FullHttpRequest request);
}
