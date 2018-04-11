package com.willkamp.server.rest;

public class RestServerApplication {


    public static void main(String[] args) throws Exception {
        final int port;
        final boolean useTls;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
            if (args.length > 1) {
                useTls = Boolean.parseBoolean(args[1]);
            } else {
                useTls = true;
            }
        } else {
            System.out.println("Using default port: 8443");
            useTls = true;
            port = 8443;
        }
        System.out.println("Serving content on port: " + port);
        RestServer server = new RestServer(port, useTls);
        server.start();
    }
}
