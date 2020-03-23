package com.being.catalina;

public enum LifecycleState {
    NEW(false, null),
    INITIALIZING(false, Lifecycle.INIT_EVENT),
    STARTING(true, Lifecycle.START_EVENT),
    STOPPING(false, Lifecycle.STOP_EVENT);

    LifecycleState(boolean available, String lifecycleEvent) {
        this.available = available;
        this.lifecycleEvent = lifecycleEvent;

    }

    private final boolean available;
    private final String lifecycleEvent;

    public boolean isAvailable() {
        return available;
    }

    public String getLifecycleEvent() {
        return lifecycleEvent;
    }

}
