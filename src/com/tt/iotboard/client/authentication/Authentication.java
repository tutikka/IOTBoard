package com.tt.iotboard.client.authentication;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Client authentication contract
 *
 * @author Tuomas Tikka
 */
public abstract class Authentication {

    /**
     * Constructor
     */
    public Authentication() {
    }

    /**
     * Return the HTTP request headers implementing the authentication
     *
     * @param connection The HTTP connection to the server
     *
     * @return The HTTP request headers
     */
    public abstract Map<String, String> getRequestHeaders(HttpURLConnection connection);

}
