package com.next.engine.model;

import com.next.engine.annotations.internal.Experimental;
import com.next.engine.event.EventCollector;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionEffect;
import lombok.Setter;

/**
 * {@link Experimental} <br/>
 * Provides a Prop that can receive functional parameters to define collision behaviors.
 * Set {@link #onCollision}, {@link #onEnter} and {@link #onExit} to define the desired behavior.
 * (See {@link CollisionEffect} for more info)
 */
@Experimental
public class ScriptedProp extends Prop {

    @Setter private CollisionEffect onCollision;
    @Setter private CollisionEffect onEnter;
    @Setter private CollisionEffect onExit;

    @Override
    public void onCollision(Body other, EventCollector collector) {
        if (onCollision == null) return;
        onCollision.resolve(other, collector);
    }

    @Override
    public void onEnter(Body other, EventCollector collector) {
        if (onEnter == null) return;
        onEnter.resolve(other, collector);
    }

    @Override
    public void onExit(Body other, EventCollector collector) {
        if (onExit == null) return;
        onExit.resolve(other, collector);
    }
}
