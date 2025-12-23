package com.next.engine.data;

import com.next.engine.event.GameEvent;
import com.next.engine.physics.MotionQueue;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.util.DoubleBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A {@code Mailbox} is a bus of relevant requests that entities must submit to the many systems of the application.
 * @value {@code motionQueue} holds the intended movements of entities (see {@link MotionQueue}).
 * @value {@code render} is a double buffered queue of requests to be submitted to the renderer pipeline
 *        (see {@link RenderQueue}).
 */
public class Mailbox {
    public final List<Supplier<? extends GameEvent>> eventSuppliers = new ArrayList<>();

    public final MotionQueue motionQueue = new MotionQueue();
    public final DoubleBuffer<RenderQueue> render = new DoubleBuffer<>(RenderQueue::new);

    public void clearAll() {
        eventSuppliers.clear();

        motionQueue.clear();
    }

    /**
     * Swaps every double buffer. Should only be called when the update is 100% done.
     */
    public void swap() {
        render.swap();
        render.write().clear();
    }

}
