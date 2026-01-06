package com.next.engine.debug;

public final class DebugTimers {
    private DebugTimers() {}

    public static final DebugTimer RENDERER = new DebugTimer(120);
    public static final DebugTimer LIGHTS = new DebugTimer(120);
    public static final DebugTimer TILES = new DebugTimer(120);
    public static final DebugTimer GAME = new DebugTimer(120);
}
