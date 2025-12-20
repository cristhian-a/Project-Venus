package com.next.util;

public class TimeAccumulator {

    private double accumulator;

    public void update(double deltaTime) {
        accumulator += deltaTime;
    }

    public double getDeltaTime() {
        return accumulator;
    }

    public void reset() {
        accumulator = 0;
    }
}
