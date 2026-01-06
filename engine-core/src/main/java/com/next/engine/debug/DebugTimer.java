package com.next.engine.debug;

public final class DebugTimer {

    private final TimeStat stat;
    private long start;

    public DebugTimer(int window) {
        stat = new TimeStat(window);
    }

    public void begin() {
        start = System.nanoTime();
    }

    public void end() {
        stat.add(System.nanoTime() - start);
    }

    public TimeStat stat() {
        return stat;
    }
}
