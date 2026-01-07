package com.next.engine.debug;

import java.util.HashMap;
import java.util.Map;

public final class DebugTimers {
    private DebugTimers() {}

    public static final String RENDER_LIGHTS = "render.lights";
    public static final String RENDER_TILES = "render.tiles";
    public static final String UPDATE = "director.update";
    public static final String RENDERER = "render.total";
    public static final String PHYSICS = "physics.total";

    private static final Map<String, DebugTimer> TIMERS = new HashMap<>();

    static {
        TIMERS.put(RENDER_LIGHTS, new DebugTimer(120));
        TIMERS.put(RENDER_TILES, new DebugTimer(120));
        TIMERS.put(RENDERER, new DebugTimer(120));
        TIMERS.put(UPDATE, new DebugTimer(120));
        TIMERS.put(PHYSICS, new DebugTimer(120));
    }

    public static DebugTimerScope scope(String id) {
        DebugTimer t = TIMERS.computeIfAbsent(id, _ -> new DebugTimer(120));
        return new DebugTimerScope(t);
    }

    public static DebugTimer of(String id) { return TIMERS.get(id); }
}
