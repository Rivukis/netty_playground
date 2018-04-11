package com.willkamp.server.rest.resthandler;

import com.google.common.collect.ImmutableMap;
import com.willkamp.server.rest.channelhandler.RequestHandler;
import com.willkamp.server.rest.model.Pojo;
import io.netty.handler.codec.http.HttpResponseStatus;

public class RestHandlers {
    public ImmutableMap<String, RequestHandler> handlers() {
        return ImmutableMap.of(
                "/api/v1/home", responseBuilder -> responseBuilder.setBody(new Pojo("i'm a title"))
                        .setStatus(HttpResponseStatus.OK));
    }
}
