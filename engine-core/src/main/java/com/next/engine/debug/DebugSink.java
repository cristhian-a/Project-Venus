package com.next.engine.debug;

import com.next.engine.physics.AABB;

public interface DebugSink {
    void box(String id, AABB bounds, DebugChannel channel);
    void text(String id, String text, int x, int y, DebugChannel channel);
    void value(String id, long value, int x, int y, DebugChannel channel);
    void value(String id, double value, int x, int y, DebugChannel channel);
}
