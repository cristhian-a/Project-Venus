package com.next.engine.data;

import com.next.engine.event.GameEvent;
import com.next.engine.physics.Movement;
import com.next.graphics.RenderQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A {@code Mailbox} is a bus of relevant requests that entities must submit to the many systems of the application.
 * @value {@code moveRequests} holds the intended movements of entities (see {@link Movement}).
 * @value {@code renderQueue} holds requests to be consumed by a renderer (see {@link RenderQueue}).
 */
public class Mailbox {
    public final List<Supplier<? extends GameEvent>> eventSuppliers = new ArrayList<>();
    public final List<Movement> moveRequests = new ArrayList<>();
    public final RenderQueue renderQueue = new RenderQueue();

    public void clearAll() {
        eventSuppliers.clear();
        moveRequests.clear();
        renderQueue.clear();
    }
}
