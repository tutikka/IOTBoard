package com.tt.iotboard.server.authentication;

import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;


public class BasicAccessAuthentication extends Authentication {

    private static final Logger L = LoggerFactory.getLogger(BasicAccessAuthentication.class);

    @Override
    public boolean validateRequestHeaders(HttpServerRequest request) {

        String username = options.get("username");
        if (username == null || username.isEmpty()) {
            return (false);
        }

        String password = options.get("password");
        if (password == null || password.isEmpty()) {
            return (false);
        }

        String header = request.getHeader("Authorization");
        if (header == null || header.isEmpty() || !header.toLowerCase().startsWith("basic ")) {
            return (false);
        }

        try {
            String auth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes("UTF-8"));
            return (auth.equals(header.split(" ")[1]));
        } catch (Exception e) {
            return (false);
        }
    }

}
