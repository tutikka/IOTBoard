package com.tt.iotboard.client.authentication;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Client-side implementation of basic access authentication
 *
 * @author Tuomas Tikka
 */
public class BasicAccessAuthentication extends Authentication {

    // username
    private String username;

    // password
    private String password;

    /**
     * Constructor
     *
     * @param username The username
     * @param password The password
     */
    public BasicAccessAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("Authorization", "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes("UTF-8")));
        } catch (Exception e) {
            // TODO
        }
        return (map);
    }

}
