package com.tt.iotboard.client.authentication;

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
     * @return The HTTP request headers
     */
    public abstract Map<String, String> getRequestHeaders();

}
