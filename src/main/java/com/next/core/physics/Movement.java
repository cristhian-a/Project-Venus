package com.next.core.physics;

import com.next.model.Actor;

public record Movement(
        Actor actor,
        float dx, float dy, float dz
) {
}
