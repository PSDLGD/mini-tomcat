package com.being.catalina;

public interface Server extends Lifecycle {


    void addService(Service service);

    Service findService(String name);

    Service[] findServices();

    void removeService(Service service);


}
