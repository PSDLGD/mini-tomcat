package com.being.catalina.core;

import com.being.catalina.Lifecycle;
import com.being.catalina.LifecycleState;
import com.being.catalina.Server;
import com.being.catalina.Service;
import com.being.catalina.util.LifecycleBase;

import java.util.stream.Stream;

public final class StandardServer extends LifecycleBase implements Server {

    private Service services[] = new Service[0];
    private final Object servicesLock = new Object();


    @Override
    protected void initInternal() {
        Stream.of(services).forEach(Lifecycle::init);
    }

    @Override
    protected void startInternal() {
        setState(LifecycleState.STARTING);
        synchronized (servicesLock) {
            Stream.of(services).forEach(Lifecycle::start);
        }
    }

    @Override
    protected void stopInternal() {

    }

    @Override
    protected void destroyInternal() {

    }

    @Override
    public void addService(Service service) {
        service.setServer(this);
        synchronized (servicesLock) {
            Service results[] = new Service[services.length + 1];
            System.arraycopy(services, 0, results, 0, services.length);
            results[services.length] = service;
            services = results;
            if (getState().isAvailable()) {
                service.start();
            }
        }
    }

    @Override
    public Service findService(String name) {
        if (name == null) {
            return null;
        }
        synchronized (servicesLock) {
            return Stream.of(services).filter(service -> service.getName().equals(name)).findAny().orElse(null);
        }

    }

    @Override
    public Service[] findServices() {
        return services;
    }

    @Override
    public void removeService(Service service) {
        synchronized (servicesLock) {
            int j = -1;
            for (int i = 0; i < services.length; i++) {
                if (services[i] == service) {
                    j = i;
                    break;
                }
            }
            if (j < 0) {
                return;
            }
            services[j].stop();
            int k = 0;
            Service results[] = new Service[services.length - 1];
            for (int i = 0; i < services.length; i++) {
                if (i != j) {
                    results[k++] = services[i];
                }
            }
            services = results;
        }
    }
}
