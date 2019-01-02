package com.tt.iotboard.server.authentication;

import com.tt.iotboard.common.Utilities;
import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class TokenAuthentication extends Authentication {

    private static final Logger L = LoggerFactory.getLogger(TokenAuthentication.class);

    @Override
    public boolean validateRequestHeaders(HttpServerRequest request, String body) {
        L.trace("validateRequestHeaders");

        String secret = options.get("secret");
        if (secret == null || secret.isEmpty()) {
            return (false);
        }
        L.info("server requires secret " + mask(secret));

        String client = request.getHeader("Authorization");
        if (client == null || client.isEmpty() || !client.toLowerCase().startsWith("iotboard ")) {
            return (false);
        }
        L.info("client sent authorization token " + client);

        String method = request.rawMethod();
        L.info("method = " + method);

        String contentType = request.getHeader("Content-Type");
        L.info("content type = " + contentType);

        String date = request.getHeader("Date");
        try {
            Date d = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").parse(date);
            // future
            if (d.getTime() > System.currentTimeMillis()) {
                return (false);
            }
            // expired
            if (System.currentTimeMillis() - d.getTime() > 15 * 60 * 1000) {
                return (false);
            }
        } catch (Exception e) {
            L.error("error authenticating client request: " + e.getMessage());
            return (false);
        }
        L.info("date = " + date);

        String uri = request.uri();
        L.info("uri = " + uri);

        String contentMD5 = request.getHeader("Content-MD5");
        L.info("content md5 = " + contentMD5);

        try {

            StringBuilder toSign = new StringBuilder();
            toSign.append(method);
            toSign.append("\n");
            toSign.append(contentType);
            toSign.append("\n");
            toSign.append(date);
            toSign.append("\n");
            toSign.append(uri);
            toSign.append("\n");
            toSign.append(contentMD5);

            String server = Base64.getEncoder().encodeToString(Utilities.HmacSHA256(secret, toSign.toString()).getBytes("UTF-8"));
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
