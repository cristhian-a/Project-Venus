package com.next.core.physics;

import com.next.model.Actor;

public record CollisionResult(
        Type type,
        Actor other,
        float resolvedDx,
        float resolvedDy
) {
    public enum Type { NONE, BLOCK, TRIGGER }
}
