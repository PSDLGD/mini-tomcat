package com.being.catalina.startup;

import com.being.catalina.Server;

import java.util.Optional;

public class Catalina {

    protected ClassLoader parentClassLoader = Catalina.class.getClassLoader();

    protected Server server = null;

    protected boolean loaded = false;

    public void start() {
        if (getServer() == null) {
            load();
        }

    }

    public void load() {
        if (loaded) {
            return;
        }
        
    }

    public void setParentClassLoader(ClassLoader parentClassLoader) {
        this.parentClassLoader = parentClassLoader;
    }

    public ClassLoader getParentClassLoader() {
        return Optional.ofNullable(parentClassLoader)
                .orElse(ClassLoader.getSystemClassLoader());
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
