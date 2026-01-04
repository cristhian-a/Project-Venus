package com.next.engine.model;

import com.next.engine.event.TriggerRule;
import com.next.engine.physics.Body;
import com.next.engine.event.EventCollector;

/**
 * Consider using {@link Sensors#singleUse} instead.
 * This subclass exists mainly for documentation purposes.
 * A single-use sensor fires only once and then disappears.
 */
public class SingleUseSensor extends Sensor {

    protected boolean active = true;

    public SingleUseSensor(float worldX, float worldY, float width, float height, TriggerRule rule) {
        super(worldX, worldY, width, height, rule);
    }

    @Override
    public void onCollision(Body other, EventCollector collector) {
        if (!active || !rule.shouldFire(this, other))
            return;

        collector.post(() -> {
            this.dispose();
            active = false;
            return rule.getEvent(this, other);
        });
    }
}
