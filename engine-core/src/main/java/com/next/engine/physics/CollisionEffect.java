package com.next.engine.physics;

import com.next.engine.annotations.internal.Experimental;
import com.next.engine.event.EventCollector;

/**
 * {@link Experimental} <br/>
 * Provides a function to be called whenever a collision occurs. {@code CollisionEffect}'s main purpose is to be set as
 * rules to be called by any {@link Body}'s implementation, specially for {@link Body#onCollision}, {@link Body#onEnter}
 * and {@link Body#onExit}.
 */
@Experimental
@FunctionalInterface
public interface CollisionEffect {
    void resolve(Body other, EventCollector collector);
}
