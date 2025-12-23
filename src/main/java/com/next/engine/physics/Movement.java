package com.next.engine.physics;

import com.next.engine.model.Actor;

public record Movement(
        Actor actor,
        float dx, float dy, float dz
) {
}
