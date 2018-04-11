package com.willkamp.server.rest;

import com.google.common.collect.ImmutableMap;
import io.netty.handler.codec.http.HttpResponseStatus;

class AppHandlers {
    ImmutableMap<String, RequestHandler> handlers() {
        return ImmutableMap.of(
                "/api/v1/home", responseBuilder -> responseBuilder.setBody(new Pojo("i'm a title"))
                        .setStatus(HttpResponseStatus.OK));
    }
}
