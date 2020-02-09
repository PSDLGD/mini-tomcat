package com.being.catalina.startup;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

public class CatalinaProperties {

    private static Properties properties = null;

    static {
        loadProperties();
    }

    public static String getProperty(String name) {
        return properties.getProperty(name);
    }


    private static void loadProperties() {
        InputStream is = null;
        try {
            String configUrl = System.getProperty("catalina.config");
            if (configUrl != null) {
                is = (new URL(configUrl)).openStream();
            }
        } catch (Throwable t) {
            handleThrowable(t);
        }
        if (is == null) {
            try {
                is = CatalinaProperties.class.getResourceAsStream("/com/being/catalina/startup/catalina.properties");
            } catch (Throwable t) {
                handleThrowable(t);
            }
        }
        if (is != null) {
            try {
                properties = new Properties();
                properties.load(is);
            } catch (Throwable t) {
                handleThrowable(t);
            }
        }
        if ((is == null)) {
            // Do something
            System.out.println("Failed to load catalina.properties");
            // That's fine - we have reasonable defaults.
            properties = new Properties();
        }
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String name = (String) enumeration.nextElement();
            String value = properties.getProperty(name);
            if (value != null) {
                System.setProperty(name, value);
            }
        }
    }

    private static void handleThrowable(Throwable t) {
        if (t instanceof ThreadDeath) {
            throw (ThreadDeath) t;
        }
        if (t instanceof VirtualMachineError) {
            throw (VirtualMachineError) t;
        }
        // All other instances of Throwable will be silently swallowed
    }


}
