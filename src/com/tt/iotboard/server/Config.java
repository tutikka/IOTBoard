package com.tt.iotboard.server;

import com.tt.iotboard.server.authentication.Authentication;
import com.tt.iotboard.server.authentication.NoAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.Properties;

public class Config {

    private static final Logger L = LoggerFactory.getLogger(Config.class);

    private int port = 9090;

    private Authentication authentication = new NoAuthentication();

    private static Config instance;

    private Config() {
        L.trace("Config");
    }

    public static Config getInstance() {
        L.trace("getInstance");
        if (instance == null) {
            L.info("creating new instance");
            instance = new Config();
        }
        return (instance);
    }

    public void fromFile(File file) throws Exception {
        L.trace("fromFile");

        if (file == null) {
            throw new Exception("config file is NULL");
        }

        L.info("reading config from " + file.getAbsolutePath());

        if (!file.exists()) {
            throw new Exception("config file " + file.getAbsolutePath() + " does not exist");
        }

        if (file == null || !file.exists() || !file.canRead()) {
            throw new Exception("config file " + file.getAbsolutePath() + " is not readable");
        }

        Properties properties = new Properties();
        properties.load(new FileReader(file));

        // port
        try {
            port = Integer.parseInt(properties.getProperty("iotboard.port"));
            L.info("port = " + port);
        } catch (Exception e) {
            L.warn("error reading port configuration: " + e.getMessage());
        }

        // authentication
        try {
            Class clazz = getClass().getClassLoader().loadClass(properties.getProperty("iotboard.authentication"));
            Constructor constructor = clazz.getConstructor();
            authentication = (Authentication) constructor.newInstance();
            authentication.setOptions(properties.getProperty("iotboard.authentication.options"));
            L.info("authentication = " + authentication.getClass().getName());
        } catch (Exception e) {
            L.warn("error reading authentication configuration: " + e.getMessage());
        }

    }

    public int getPort() {
        return port;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

}
