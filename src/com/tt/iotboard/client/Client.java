package com.tt.iotboard.client;

import com.tt.iotboard.client.authentication.Authentication;
import com.tt.iotboard.client.authentication.NoAuthentication;
import com.tt.iotboard.client.authentication.TokenAuthentication;
import com.tt.iotboard.common.Utilities;
import io.vertx.core.json.Json;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * Java reference client to send measurements to the server
 *
 * @author Tuomas Tikka
 */
public class Client {

    // remote hostname or ip address (server)
    private String host;

    // remote port number
    private int port;

    // how many milliseconds to wait for a connection to remote host before throwing an exception
    private int connectTimeout;

    // how many milliseconds to wait for a response from remote host before throwing an exception
    private int readTimeout;

    // http proxy to use when connecting to remote host
    private Proxy proxy;

    // client authentication
    private Authentication authentication;

    /**
     * Builder-pattern for initializing the client
     */
    public static class Builder {

        // default host
        private String host = "localhost";

        // default port
        private int port = 9090;

        // defalut connect timeout
        private int connectTimeout = 10000;

        // default read timeout
        private int readTimeout = 10000;

        // default proxy (no proxy)
        private Proxy proxy = Proxy.NO_PROXY;

        // default authentication (no authentication)
        private Authentication authentication = new NoAuthentication();

        /**
         * Constructor
         */
        public Builder() {
        }

        /**
         * Set the remote hostname or ip address
         *
         * @param host The hostname or ip address
         * @return A builder with the hostname or ip address set
         */
        public Builder host(String host) {
            this.host = host;
            return (this);
        }

        /**
         * Set the remote port number
         *
         * @param port The port number
         * @return A builder with the port number set
         */
        public Builder port(int port) {
            this.port = port;
            return (this);
        }

        /**
         * Set the number of milliseconds to wait for a connection to the remote host before throwing an exception
         *
         * @param connectTimeout The connect timeout in milliseconds
         * @return A builder with the connect timeout set
         */
        public Builder connectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return (this);
        }

        /**
         * Set the number of milliseconds to wait for a response from the remote host before throwing an exception
         *
         * @param readTimeout The read timeout in milliseconds
         * @return A builder with the read timeout set
         */
        public Builder readTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return (this);
        }

        /**
         * Set the http proxy to use when connecting to the remote host
         *
         * @param proxy The http proxy
         * @return A builder with the http proxy set
         */
        public Builder proxy(Proxy proxy) {
            this.proxy = proxy;
            return (this);
        }

        /**
         * Set the client authentication
         *
         * @param authentication The client authentication
         * @return A builder with the client authentication set
         */
        public Builder authentication(Authentication authentication) {
            this.authentication = authentication;
            return (this);
        }

        /**
         * Return a usable instance of the client
         *
         * @return The client instance
         */
        public Client build() {
            return (new Client(host, port, connectTimeout, readTimeout, proxy, authentication));
        }

    }

    /**
     * Constructor
     *
     * @param host The remote hostname or ip address
     * @param port The remote port number
     * @param connectTimeout The connect timeout
     * @param readTimeout The read timeout
     * @param proxy The http proxy
     * @param authentication The client authentication
     */
    private Client(String host, int port, int connectTimeout, int readTimeout, Proxy proxy, Authentication authentication) {
        this.host = host;
        this.port = port;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.proxy = proxy;
        this.authentication = authentication;
    }

    /**
     * Send a batch of measurements to the server
     *
     * @param measurements The batch of measurements
     * @throws Exception If a problem occurs while sending the measurements to the server
     */
    public void send(Measurement... measurements) throws Exception {
        URL url = new URL("http://" + host + ":" + port + "/api/v1/measurement");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(connectTimeout);
        connection.setReadTimeout(readTimeout);
        String json = Json.encode(measurements);
        connection.addRequestProperty("Date", new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").format(new Date())); // Example: Wed, 02 Jan 2019 07:32:49 GMT
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("Content-Length", "" + json.length());
        connection.addRequestProperty("Content-MD5", Utilities.MD5(json));
        Map<String, String> headers = authentication.getRequestHeaders(connection);
        for (String name : headers.keySet()) {
            String value = headers.get(name);
            connection.addRequestProperty(name, value);
        }
        connection.getOutputStream().write(json.getBytes("UTF-8"));
        System.out.println("> " + json);
        int status = connection.getResponseCode();
        System.out.println("< " + status);
        connection.disconnect();
    }

    public static void main(String[] args) {

        final int numMeasurements = 20;

        final Random random = new Random(System.currentTimeMillis());

        final Client client = new Builder()
                .host("localhost")
                .port(9090)
                .connectTimeout(10000)
                .readTimeout(10000)
                .authentication(new TokenAuthentication("abcd"))
                .build();

        Thread sensor1 = new Thread() {
            @Override
            public void run() {
                float baseTemperature = 25.0f;
                float baseHumidity = 50.0f;

                for (int i = 0; i < numMeasurements; i++) {

                    Measurement measurement = new Measurement("Sensor #0001 (Living Room Temp)", System.currentTimeMillis(), 10);

                    measurement.getValues().add(new Value("Temperature", baseTemperature));
                    measurement.getValues().add(new Value("Humidity", baseHumidity));

                    try {
                        Thread.sleep(1000 + random.nextInt(2000));
                        client.send(measurement);
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }

                    baseTemperature += (random.nextFloat() - 0.5f) * 2.5f;
                    baseHumidity += (random.nextFloat() - 0.5f) * 5.0f;
                }
            }
        };
        sensor1.start();

        Thread sensor2 = new Thread() {
            @Override
            public void run() {
                float baseTemperature = 10.0f;
                float baseHumidity = 75.0f;

                for (int i = 0; i < numMeasurements; i++) {

                    Measurement measurement = new Measurement("Sensor #0002 (Garage Temp)", System.currentTimeMillis(), 10);

                    measurement.getValues().add(new Value("Temperature", baseTemperature));
                    measurement.getValues().add(new Value("Humidity", baseHumidity));

                    try {
                        Thread.sleep(1000 + random.nextInt(2000));
                        client.send(measurement);
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }

                    baseTemperature += (random.nextFloat() - 0.5f) * 2.5f;
                    baseHumidity += (random.nextFloat() - 0.5f) * 5.0f;
                }
            }
        };
        sensor2.start();

        Thread sensor3 = new Thread() {
            @Override
            public void run() {
                float baseTemperature = -5.0f;
                float baseHumidity = 25.0f;

                for (int i = 0; i < numMeasurements; i++) {

                    Measurement measurement = new Measurement("Sensor #0003 (Basement Temp)", System.currentTimeMillis(), 10);

                    measurement.getValues().add(new Value("Temperature", baseTemperature));
                    measurement.getValues().add(new Value("Humidity", baseHumidity));

                    try {
                        Thread.sleep(1000 + random.nextInt(2000));
                        client.send(measurement);
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }

                    baseTemperature += (random.nextFloat() - 0.5f) * 2.5f;
                    baseHumidity += (random.nextFloat() - 0.5f) * 5.0f;
                }
            }
        };
        sensor3.start();
    }

}
