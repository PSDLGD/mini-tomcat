package com.being.catalina;

import java.util.EventObject;

public final class LifecycleEvent extends EventObject {

    private final Object data;

    private final String type;

    public LifecycleEvent(Lifecycle lifecycle, String type, Object data) {
        super(lifecycle);
        this.type = type;
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }

    public String getType() {
        return this.type;
    }

    public Lifecycle getLifecycle() {
        return (Lifecycle) getSource();
    }
}
