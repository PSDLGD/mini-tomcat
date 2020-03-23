package com.being.catalina;

@FunctionalInterface
public interface LifecycleListener {

    void lifecycleEvent(LifecycleEvent event);

}
