package com.willkamp.server;

public class Application {

    public static void main(String[] args) throws Exception {
        final int port;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else {
            System.out.println("Using default port: 8080");
            port = 8080;
        }
        EchoServer echoServer = new EchoServer(port);
        echoServer.start();
    }
}
