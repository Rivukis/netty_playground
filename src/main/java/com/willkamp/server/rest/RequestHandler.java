package com.willkamp.server.rest;

public interface RequestHandler {
    ResponseBuilder request(ResponseBuilder responseBuilder) throws Exception;
}
