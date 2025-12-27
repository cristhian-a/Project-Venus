package com.next.engine.event;

import com.next.engine.model.Sensor;
import com.next.engine.physics.Body;

public interface TriggerRule {
    boolean shouldFire(Sensor self, Body other);
    GameEvent getEvent(Sensor self, Body other);
}
