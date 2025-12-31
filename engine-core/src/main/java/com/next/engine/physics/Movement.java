package com.next.engine.physics;

import com.next.engine.model.Actor;

/**
 * @deprecated use plain {@link MotionQueue} instead
 */
@Deprecated(forRemoval = true)
public record Movement(
        Actor actor,
        float dx, float dy, float dz
) {
}
