package com.tt.iotboard.server.authentication;

import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;


public class BasicAccessAuthentication extends Authentication {

    private static final Logger L = LoggerFactory.getLogger(BasicAccessAuthentication.class);

    @Override
    public boolean validateRequestHeaders(HttpServerRequest request, String body) {
        L.trace("validateRequestHeaders");

        String username = options.get("username");
        if (username == null || username.isEmpty()) {
            return (false);
        }
        L.info("server requires username " + username);

        String password = options.get("password");
        if (password == null || password.isEmpty()) {
            return (false);
        }
        L.info("server requires password " + mask(password));

        String client = request.getHeader("Authorization");
        if (client == null || client.isEmpty() || !client.toLowerCase().startsWith("basic ")) {
            return (false);
        }
        L.info("client sent authorization token " + client);

        try {
            String server = Base64.getEncoder().encodeToString((username + ":" + password).getBytes("UTF-8"));
            boolean authenticated = server.equals(client.split(" ")[1]);
            if (authenticated) {
                L.info("access granted to client");
            } else {
                L.warn("access denied to client (client token = " + client + ", server token = " + server + ")");
            }

            return (authenticated);
        } catch (Exception e) {
            L.error("error authenticating client request: " + e.getMessage());
            return (false);
        }
    }

}
