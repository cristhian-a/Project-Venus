package com.next.engine.model;

import com.next.engine.event.TriggerRule;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionResult;

public class SingleUseSensor extends Sensor {

    public SingleUseSensor(float worldX, float worldY, float width, float height, TriggerRule rule) {
        super(worldX, worldY, width, height, rule);
    }

    @Override
    public CollisionResult onCollision(Body other) {
        if (!active || !rule.shouldFire(this, other))
            return null;

        return new CollisionResult(() -> {
            this.dispose();
            return rule.getEvent(this, other);
        });
    }
}
