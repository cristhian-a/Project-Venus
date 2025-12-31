package com.next.engine;

import lombok.Getter;

public final class Global {

    private Global() {}

    public static int targetFPS = 60;
    public static double fixedDelta = 1d / 60d;

    // Rendering related stuff
    /**
     * The base factor to be considered during calculations of alpha when rendering lights. A higher base factor means
     * lights eliminate more darkness, and a lower base factor preserves more darkness. Value should be either 0, 1, or
     * in between.
     */
    public static float alphaBaseFactor = 0.8f;

    /**
     * Sum of all delta times since the start of the main loop.
     */
    @Getter private static double time;

    static void accumulateTime(double delta) { time += delta; }

}
