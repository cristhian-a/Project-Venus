package com.next.core.data;

import com.next.core.event.GameEvent;
import com.next.core.physics.Movement;
import com.next.graphics.RenderQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@code Mailbox} is a bus of relevant requests that entities must submit to the many systems of the application.
 * @value {@code moveRequests} holds the intended movements of entities (see {@link Movement}).
 * @value {@code renderQueue} holds requests to be consumed by a renderer (see {@link RenderQueue}).
 */
public class Mailbox {
    public final List<Movement> moveRequests = new ArrayList<>();
    public final RenderQueue renderQueue = new RenderQueue();
    public final List<GameEvent> events = new ArrayList<>();

    public void clearAll() {
        moveRequests.clear();
        renderQueue.clear();
        events.clear();
    }
}
