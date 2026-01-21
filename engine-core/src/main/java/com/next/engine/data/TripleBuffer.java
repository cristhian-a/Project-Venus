package com.next.engine.data;

import java.util.function.Supplier;

/**
 * This class works similarly to {@link MultiBufferStrategy}, but its rotation is performed with three fixed slots instead
 * of using an inner <i>queue</i>.
 * <p>
 * Important: the same thread safety issues apply as in {@link MultiBufferStrategy}. Its intended use is the same.
 * @param <T> a {@link Buffered} type, often a data table.
 * @see MultiBufferStrategy
 * @see Buffered
 * @see DoubleBuffer
 * @see BufferStrategy
 */
public final class TripleBuffer<T extends Buffered> implements BufferStrategy<T> {
    private volatile T read;
    private T write;
    private T spare;

    /**
     * Creates a new {@code TripleBuffer} strategy with the given factory.
     * <p>
     * Example usage:
     * <pre><code>
     *     var buffer = new TripleBuffer<>(RenderQueue::new);
     *     // or
     *     var buffer = new TripleBuffer<>(() -> new RenderQueue());
     * </code></pre>
     * @param factory a {@link Supplier} that creates a new buffer.
     * @see Buffered
     */
    public TripleBuffer(Supplier<T> factory) {
        this.read = factory.get();
        this.write = factory.get();
        this.spare = factory.get();
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

    @Override
    public void swap() {
        T newRead  = write;
        T newWrite = spare;
        T newSpare = read;

        write = newWrite;
        spare = newSpare;
        read = newRead;
    }
}
