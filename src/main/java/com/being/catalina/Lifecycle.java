package com.being.catalina;

public interface Lifecycle {

    String INIT_EVENT = "init";

    String START_EVENT = "start";

    String STOP_EVENT = "stop";

    void addLifecycleListener(LifecycleListener listener);

    void removeLifecycleListener(LifecycleListener listener);

    LifecycleListener[] findLifecycleListeners();

    LifecycleState getState();

    String getStateName();

    void init();

    void start();

    void stop();

    void destroy();


}
