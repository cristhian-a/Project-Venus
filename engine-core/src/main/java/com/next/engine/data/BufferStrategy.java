package com.next.engine.data;

public interface BufferStrategy<T extends Buffered> {
    T beginFrame();
    T surface();
    void swap();
}
