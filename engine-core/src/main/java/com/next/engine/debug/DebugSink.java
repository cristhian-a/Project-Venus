package com.next.engine.debug;

public interface DebugSink {
    void text(String id, String text, int x, int y, DebugChannel channel);
    void value(String id, long value, int x, int y, DebugChannel channel);
    void value(String id, double value, int x, int y, DebugChannel channel);
}
