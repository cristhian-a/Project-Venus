package com.next.engine.util;

public class Stopwatch {

    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime = System.nanoTime();
    }

    public long elapsedTime() {
        return endTime - startTime;
    }

    public void reset() {
        startTime = System.nanoTime();
    }
}
