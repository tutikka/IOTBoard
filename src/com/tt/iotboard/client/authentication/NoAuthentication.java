package com.tt.iotboard.client.authentication;

import java.util.HashMap;
import java.util.Map;

public class NoAuthentication extends Authentication {

    @Override
    public Map<String, String> getRequestHeaders() {
        return (new HashMap<>());
    }

}
