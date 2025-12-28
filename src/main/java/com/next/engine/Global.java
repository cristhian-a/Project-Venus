package com.next.engine;

import lombok.Getter;

public final class Global {

    private Global() {}

    public static int targetFPS = 60;
    public static double fixedDelta = 1d / 60d;

    /**
     * Sum of all delta times since the start of the main loop.
     */
    @Getter private static double time;

    static void accumulateTime(double delta) { time += delta; }

}
