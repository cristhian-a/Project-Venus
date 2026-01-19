package com.next.engine.model;

import com.next.engine.event.TriggerRule;
import com.next.engine.physics.Body;
import com.next.engine.event.EventCollector;

/**
 * Consider using {@link Sensors#singleUse} instead.
 * This subclass exists mainly for documentation purposes.
 * <br/>
 * A single-use sensor fires only once and then disappears.
 */
public class SingleUseSensor extends Sensor {

    protected boolean active = true;

    public SingleUseSensor(float worldX, float worldY, float width, float height, TriggerRule onCollision) {
        super(worldX, worldY, width, height, onCollision);
    }

    @Override
    public void onCollision(Body other, EventCollector collector) {
        if (!active || !onCollision.shouldFire(this, other))
            return;

        collector.post(() -> {
            this.dispose();
            active = false;
            return onCollision.getEvent(this, other);
        });
    }
}
