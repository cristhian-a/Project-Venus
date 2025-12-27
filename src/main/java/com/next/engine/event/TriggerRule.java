package com.next.engine.event;

import com.next.engine.model.Sensor;
import com.next.engine.physics.Body;

/**
 * Trigger rules currently are stateful, so take care when sharing one with multiple instances, as a single instance
 * might invalidate all the other ones.
 */
public interface TriggerRule {
    boolean shouldFire(Sensor self, Body other);
    GameEvent getEvent(Sensor self, Body other);
}
