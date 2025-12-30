package com.next.engine.data;

import java.util.function.Supplier;

public final class DoubleBuffer<T> {
    private volatile T read;
    private T write;

    public DoubleBuffer(Supplier<T> factory) {
        this.read = factory.get();
        this.write = factory.get();
    }

    public T read() { return read; }
    public T write() { return write; }

    public void swap() {
        T temp = write;
        write = read;
        read = temp;
    }
}
