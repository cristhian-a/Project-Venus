package com.next.engine.data;

import java.util.function.Supplier;

/**
 * A {@code DoubleBuffer} is a utility class that provides an implementation of a double-buffered data management
 * strategy. It allows for the separation of read and write operations by using two distinct buffers,
 * one for reading and one for writing, and provides a mechanism to swap them efficiently.
 * <br/>
 * You can consider using {@link MultiBufferStrategy} with 2 buffers to achieve the same effect, unless performance
 * is critically necessary. As this implementation doesn't need to rotate its buffers and only swaps two references, it
 * might come handy in some cases.
 * <br/>
 * The same thread safety issues apply as in {@link MultiBufferStrategy}. Its intended use is the same.
 * @param <T> the type of buffer being managed, which must implement the {@link Buffered} interface.
 * @see MultiBufferStrategy
 * @see BufferStrategy
 * @see Buffered
 */
public final class DoubleBuffer<T extends Buffered> implements BufferStrategy<T> {
    private volatile T read;
    private T write;

    /**
     * Creates a new {@code DoubleBuffer} strategy with the given factory.
     * <p>
     * Example usage:
     * <pre><code>
     *     var buffer = new DoubleBuffer<>(RenderQueue::new);
     *     // or
     *     var buffer = new DoubleBuffer<>(() -> new RenderQueue());
     * </code></pre>
     * @param factory a {@link Supplier} that creates a new buffer.
     */
    public DoubleBuffer(Supplier<T> factory) {
        this.read = factory.get();
        this.write = factory.get();
    }

    @Override
    public T beginFrame() {
        write.clear();
        return write;
    }

    @Override
    public T surface() {
        return read;
    }

    public void swap() {
        T temp = write;
        write = read;
        read = temp;
    }
}
