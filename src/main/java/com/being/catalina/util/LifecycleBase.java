package com.being.catalina.util;

import com.being.catalina.Lifecycle;
import com.being.catalina.LifecycleEvent;
import com.being.catalina.LifecycleListener;
import com.being.catalina.LifecycleState;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class LifecycleBase implements Lifecycle {

    // 记录当前的状态
    private volatile LifecycleState state = LifecycleState.NEW;

    private final List<LifecycleListener> lifecycleListeners = new CopyOnWriteArrayList<>();

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        lifecycleListeners.add(listener);
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycleListeners.remove(listener);
    }

    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return lifecycleListeners.toArray(new LifecycleListener[0]);
    }

    protected void fireLifecycleEvent(String type, Object data) {
        LifecycleEvent event = new LifecycleEvent(this, type, data);
        lifecycleListeners.forEach(listener -> listener.lifecycleEvent(event));
    }

    @Override
    public final synchronized void init() {
        setStateInternal(LifecycleState.INITIALIZING, null);
        initInternal();
    }

    protected abstract void initInternal();


    @Override
    public final synchronized void start() {
        if (state.equals(LifecycleState.NEW)) {
            init();
        } else if (state.equals(LifecycleState.STOPPING)) {
            stop();
        }
        setStateInternal(LifecycleState.STARTING, null);
        startInternal();

    }

    protected abstract void startInternal();

    @Override
    public final synchronized void stop() {
        if (state.equals(LifecycleState.NEW)) {
            state = LifecycleState.STOPPING;
            return;
        }
        setStateInternal(LifecycleState.STOPPING, null);
        stopInternal();

    }

    protected abstract void stopInternal();


    @Override
    public final synchronized void destroy() {

    }

    protected abstract void destroyInternal();


    private synchronized void setStateInternal(LifecycleState state, Object data) {
        this.state = state;
        String lifecycleEvent = state.getLifecycleEvent();
        if (lifecycleEvent != null) {
            fireLifecycleEvent(lifecycleEvent, data);
        }
    }

    protected synchronized void setState(LifecycleState state) {
        setStateInternal(state, null);

    }

    @Override
    public LifecycleState getState() {
        return state;
    }

    @Override
    public String getStateName() {
        return getState().toString();
    }
}
