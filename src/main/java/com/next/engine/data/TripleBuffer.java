package com.next.engine.data;

import java.util.function.Supplier;

public final class TripleBuffer<T> {
    private volatile T read;
    private volatile T write;
    private volatile T spare;

    public TripleBuffer(Supplier<T> factory) {
        this.read = factory.get();
        this.write = factory.get();
        this.spare = factory.get();
    }

    public T read() { return read; }
    public T write() { return write; }

    public void swap() {
        T temp = read;
        read = write;
        write = spare;
        spare = temp;
    }
}
