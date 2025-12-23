package com.next.engine.physics;

import com.next.engine.event.GameEvent;
import com.next.engine.model.Actor;

import java.util.function.Supplier;

public record CollisionResult(
        CollisionType type,
        Actor other,
        float resolvedDx,
        float resolvedDy,
        Supplier<? extends GameEvent> eventFactory
) {
}
