package com.next.core.physics;

import com.next.model.Actor;

public record CollisionEvent(
        Actor collider
) {
}
