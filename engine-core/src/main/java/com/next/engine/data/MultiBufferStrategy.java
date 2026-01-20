package com.next.engine.data;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Supplier;

/**
 * Provides a {@code BufferStrategy} implementation that uses multiple buffers to store data and rotates them on every
 * {@link #swap()} call, very similar to a ring buffer/swap chain. Double or triple buffering are the most common
 * strategy usages for this class, but you can use any number of buffers you want (provided they are more than 1).
 * <p>
 * This class is not thread safe. While calling {@link #surface()} from different threads is generally fine,
 * {@link #beginFrame()} and {@link #swap()} must be called from the same thread. The way this class is intended to be
 * used is that all the writing is done in the same thread, then its content is swapped (after writing). Reading can then
 * be performed by other systems.
 * @param <T> an implementation of {@link Buffered}, often a data table.
 * @see Buffered
 * @see BufferStrategy
 */
public final class MultiBufferStrategy<T extends Buffered> implements BufferStrategy<T> {
    private final Queue<T> spare;
    private volatile T surface;
    private T back;

    /**
     * Creates a new {@code MultiBufferStrategy} with the given number of buffers. A {@link Supplier} is required to create each
     * buffer inside the ring buffer. The number of buffers must be at least 2, otherwise an {@link IllegalArgumentException}
     * is thrown.
     * <pre><code>
     *     var buffer = new MultiBufferStrategy(2, () -> new RenderQueue());
     *     // or
     *     var buffer = new MultiBufferStrategy(3, RenderQueue::new);
     * </code></pre>
     * @param numBuffers the number of buffers to be used in this strategy.
     * @param factory a {@link Supplier} that creates a new buffer.
     *
     * @throws IllegalArgumentException if the number of buffers is less than 2.
     * @see Buffered
     */
    public MultiBufferStrategy(int numBuffers, Supplier<T> factory) {
        if (numBuffers < 2)
            throw new IllegalArgumentException("RingBuffer requires at least 2 buffers.");

        spare = new ArrayDeque<>(numBuffers);
        for (int i = 0; i < numBuffers; i++) {
            spare.add(factory.get());
        }

        surface = spare.poll();
        back = spare.poll();
    }

    /**
     * Clears the back buffer and returns it ready for writing.
     * The intended usage is to call and retrieve the back buffer during the frame's beginning and then manage it locally
     * until the swap, which is the moment when the written information is published. <br/>
     * Important: Don't call {@code beginFrame()} again until
     * writing is finished, because it will always clear the back buffer. After finishing writing, call {@link #swap()},
     * then it will be safe to call this method once again. <br/>
     * @return the back buffer ready for writing.
     */
    @Override
    public T beginFrame() {
        back.clear();
        return back;
    }

    /**
     * Provides the published data after {@link #swap()}'s rotation call. As the returned surface attribute is
     * {@code volatile}, it is safe to be read from multiple threads. (check {@link #surface})
     * <p>
     * Important: the returned value is intended to be <i>read-only</i>. If any writing is required, make sure it is done
     * only by a single thread reader.
     * @return the surface buffer ready for reading.
     */
    @Override
    public T surface() {
        return surface;
    }

    /**
     * Rotates the inner buffers and turns the back buffer into the surface one, publishing the written data.
     * <br/>
     * Intended to be called after writing is finished, during the frame's publish/end phase. If {@code swap()} is called
     * before that, its content may persist in the buffers in strange ways. <br/>
     * @throws IllegalStateException if any buffer mismanagement happened and there are no spare buffers available.
     */
    @Override
    public void swap() {
        T newSurface  = back;
        spare.offer(surface);

        back = spare.poll();
        if (back == null) throw new IllegalStateException("No spare buffers available.");

        surface = newSurface;
    }
}
