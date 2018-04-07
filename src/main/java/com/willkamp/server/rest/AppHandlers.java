package com.willkamp.server.rest;

import com.google.common.collect.ImmutableMap;
import io.netty.handler.codec.http.HttpResponseStatus;

class AppHandlers {
    ImmutableMap<String, RequestHandler> handlers() {
        return ImmutableMap.of(
                "/api/v1/home", request -> new ResponseBuilder().setBody("{}")
                        .setStatus(HttpResponseStatus.OK)
                        .setVersion(request.protocolVersion()));
    }
}
