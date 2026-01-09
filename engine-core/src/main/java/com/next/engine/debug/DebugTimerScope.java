package com.next.engine.debug;

public final class DebugTimerScope implements AutoCloseable {

    private final DebugTimer timer;

    public DebugTimerScope(DebugTimer timer) {
        this.timer = timer;
        timer.begin();
    }

    @Override
    public void close() {
        timer.end();
    }
}
