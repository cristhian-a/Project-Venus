package com.next.core.physics;

import com.next.core.event.GameEvent;
import com.next.core.model.Actor;

import java.util.List;

public record CollisionResult(
        Type type,
        Actor other,
        float resolvedDx,
        float resolvedDy,
        List<GameEvent> events
) {
    public enum Type { NONE, BLOCK, TRIGGER }
}
