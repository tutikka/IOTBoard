package com.tt.iotboard.server.authentication;

import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public abstract class Authentication {

    private static final Logger L = LoggerFactory.getLogger(Authentication.class);

    protected Map<String, String> options = new HashMap<>();

    public Authentication() {
        L.trace("Authentication");
    }

    public void setOptions(String options) {
        L.trace("setOptions");
        if (options != null) {
            StringTokenizer st = new StringTokenizer(options, ",");
            while (st.hasMoreTokens()) {
                String token = st.nextToken().trim();
                String name = token.split("=")[0].trim();
                String value = token.split("=")[1].trim();
                this.options.put(name, value);
                L.info(name + " = " + value);
            }
        } else {
            L.info("no options found for authentication");
        }
    }

    public String mask(String s) {
        L.trace("mask");
        if (s == null) {
            return (null);
        }
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            masked.append("*");
        }
        return (masked.toString());
    }

    public abstract boolean validateRequestHeaders(HttpServerRequest request, String body);

}
