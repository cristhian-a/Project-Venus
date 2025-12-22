package com.next.util;

import com.next.engine.Global;

public class TimeAccumulator {

    private double accumulator;

    public void update(double deltaTime) {
        accumulator += Global.fixedDelta; // TODO I should figure how to use deltaTime instead
    }

    public double getDeltaTime() {
        return accumulator;
    }

    public void reset() {
        accumulator = 0;
    }
}
