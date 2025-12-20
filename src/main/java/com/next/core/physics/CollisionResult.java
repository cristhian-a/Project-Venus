package com.next.core.physics;

import com.next.core.event.GameEvent;
import com.next.core.model.Actor;

import java.util.List;
import java.util.function.Supplier;

public record CollisionResult(
        CollisionType type,
        Actor other,
        float resolvedDx,
        float resolvedDy,
        Supplier<? extends GameEvent> eventFactory
) {
}
