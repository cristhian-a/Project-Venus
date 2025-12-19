package com.next.core.physics;

import com.next.core.model.Actor;

public record CollisionEvent(
        Actor collider
) {
}
