package com.next.engine.physics;

import com.next.engine.model.Actor;

@Deprecated(forRemoval = true)
public record CollisionEvent(
        Actor collider
) {
}
