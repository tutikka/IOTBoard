package com.tt.iotboard.client.authentication;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class NoAuthentication extends Authentication {

    @Override
    public Map<String, String> getRequestHeaders(HttpURLConnection connection) {
        return (new HashMap<>());
    }

}
