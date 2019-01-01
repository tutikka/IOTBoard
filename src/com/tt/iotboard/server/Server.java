package com.tt.iotboard.server;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Server {

    private static final Logger L = LoggerFactory.getLogger(Server.class);

    /**
     * Constructor
     */
    private Server() {
        L.trace("Server");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ServerVerticle());
    }

    public static void main(String[] args) {
        L.trace("main");
        if (args.length > 0) {
            try {
                Config.getInstance().fromFile(new File(args[0]));
            } catch (Exception e) {
                L.error(e.getMessage());
                e.printStackTrace(System.err);
            }
        }
        new Server();
    }

}
