package com.next.engine.physics;

import com.next.engine.event.GameEvent;

import java.util.function.Supplier;

@Deprecated
public record CollisionResult(
        Supplier<? extends GameEvent> eventFactory
) {
}
