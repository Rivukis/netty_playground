package com.willkamp.server.rest.channelhandler;

import com.willkamp.server.rest.factory.ResponseBuilder;

public interface RequestHandler {
    ResponseBuilder request(ResponseBuilder responseBuilder) throws Exception;
}
