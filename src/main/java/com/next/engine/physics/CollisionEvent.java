package com.next.engine.physics;

import com.next.engine.model.Actor;

public record CollisionEvent(
        Actor collider
) {
}
