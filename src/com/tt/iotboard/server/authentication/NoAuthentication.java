package com.tt.iotboard.server.authentication;

import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoAuthentication extends Authentication {

    private static final Logger L = LoggerFactory.getLogger(NoAuthentication.class);

    @Override
    public boolean validateRequestHeaders(HttpServerRequest request, String body) {
        L.trace("validateRequestHeaders");
        return (true);
    }

}
