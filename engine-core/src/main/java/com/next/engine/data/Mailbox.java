package com.next.engine.data;

import com.next.engine.event.GameEvent;
import com.next.engine.event.EventCollector;
import com.next.engine.physics.MotionQueue;
import com.next.engine.graphics.RenderQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A {@code Mailbox} is a bus of relevant requests that entities must submit to the many systems of the application.
 * @value {@code motionQueue} holds the intended movements of entities (see {@link MotionQueue}).
 * @value {@code render} is a double buffered queue of requests to be submitted to the renderer pipeline
 *        (see {@link RenderQueue}).
 */
public final class Mailbox implements EventCollector {

    private final BufferStrategy<RenderQueue> render = new MultiBufferStrategy<>(5, RenderQueue::new);
    public final List<Supplier<? extends GameEvent>> eventSuppliers = new ArrayList<>();
    public final MotionQueue motionQueue = new MotionQueue();

    private RenderQueue renderBackBuffer;

    /**
     * Should be called at the beginning of every frame to clear any buffer's writing queue
     */
    public void beginFrame() {
        eventSuppliers.clear();
        motionQueue.clear();

        renderBackBuffer = render.beginFrame();
    }

    /**
     * Sets every posted information in this frame visible. Call it at the end of every frame.
     */
    public void publish() {
        render.swap();
        renderBackBuffer = null;
    }

    public void post(Supplier<? extends GameEvent> supplier) {
        eventSuppliers.add(supplier);
    }

    /**
     * Gets a {@code RenderQueue} to submit render requests to.
     * @return {@link RenderQueue}
     */
    public RenderQueue postRender() {
        return renderBackBuffer;
    }

    /**
     * DO NOT submit requests to this queue, otherwise race conditions may happen.
     * @return {@link RenderQueue} with all published information to be read.
     */
    public RenderQueue receiveRender() {
        return render.surface();
    }

}
