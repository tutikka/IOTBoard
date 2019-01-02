package com.tt.iotboard.client.authentication;

import com.tt.iotboard.common.Utilities;

import java.net.HttpURLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TokenAuthentication extends Authentication {

    private String secret;

    public TokenAuthentication(String secret) {
        this.secret = secret;
    }

    @Override
    public Map<String, String> getRequestHeaders(HttpURLConnection connection) {
        Map<String, String> map = new HashMap<>();
        try {

            String method = connection.getRequestMethod();

            String contentType = connection.getRequestProperty("Content-Type");

            String date = connection.getRequestProperty("Date");

            String uri = connection.getURL().getPath() + (connection.getURL().getQuery() == null ? "" : "?" + connection.getURL().getQuery());

            String contentMD5 = connection.getRequestProperty("Content-MD5");

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

            map.put("Authorization", "IOTBoard " + Base64.getEncoder().encodeToString(Utilities.HmacSHA256(secret, toSign.toString()).getBytes("UTF-8")));
        } catch (Exception e) {
            // TODO
        }
        return (map);
    }

}
