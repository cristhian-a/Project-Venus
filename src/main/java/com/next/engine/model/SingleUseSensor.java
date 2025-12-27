package com.next.engine.model;

import com.next.engine.event.TriggerRule;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionResult;

/**
 * Consider using {@link Sensors#singleUse} instead.
 * This subclass exists mainly for documentation purposes.
 * A single-use sensor is a sensor that fires once and then disappears.
 */
public class SingleUseSensor extends Sensor {

    protected boolean active = true;

    public SingleUseSensor(float worldX, float worldY, float width, float height, TriggerRule rule) {
        super(worldX, worldY, width, height, rule);
    }

    @Override
    public CollisionResult onCollision(Body other) {
        if (!active || !rule.shouldFire(this, other))
            return null;

        return new CollisionResult(() -> {
            this.dispose();
            active = false;
            return rule.getEvent(this, other);
        });
    }
}
