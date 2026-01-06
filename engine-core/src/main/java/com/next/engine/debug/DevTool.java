package com.next.engine.debug;

public interface DevTool {

    /**
     * Called every frame during the engine's update loop.
     * Intended for data collection and sampling.
     */
    void update();

    /**
     * Called every frame to emit debug output.
     * Be cautious about heavy allocations with it, as it's designed to be an inexpensive operation.
     */
    void emit(DebugSink sink);

    DebugChannel channel();
    boolean isEnabled();
}
