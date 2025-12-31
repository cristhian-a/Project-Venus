package com.next.engine.physics;

import com.next.engine.event.GameEvent;

import java.util.function.Supplier;

public interface CollisionCollector {
    void post(Supplier<? extends GameEvent> eventFactory);
}
