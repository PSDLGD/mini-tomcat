package com.being.catalina;

public interface Service extends Lifecycle {

    void setServer(Server server);

    String getName();


}
