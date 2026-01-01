package com.next.engine.model;

import com.next.engine.annotations.internal.Experimental;
import com.next.engine.event.GameEvent;
import com.next.engine.event.TriggerRule;
import com.next.engine.physics.Body;
import org.jspecify.annotations.NonNull;

/**
 * Utility class for creating sensors.
 */
public final class Sensors {

    private Sensors() {}

    /**
     * Creates a sensor that fires only once, and then is automatically disposed.
     * @param x world X position
     * @param y world Y position
     * @param width width size
     * @param height height size
     * @param base its base trigger rule
     * @return single-use {@link Sensor}
     */
    public static Sensor singleUse(float x, float y, float width, float height, @NonNull TriggerRule base) {
        return new Sensor(x, y, width, height, once(base));
    }

    /**
     * {@link Experimental}<br/>
     * This is an idea of an implementation using query building, not done yet.
     * @apiNote It has the problem of shared state across instances that use the same policy.
     * @param base base trigger rule
     * @return a trigger rule that can be fired only once and will dispose its sensor automatically
     */
    @Experimental
    public static TriggerRule once(@NonNull TriggerRule base) {
        return new TriggerRule() {
            boolean fired = false;

            @Override
            public boolean shouldFire(Sensor self, Body other) {
                return !fired && base.shouldFire(self, other);
            }

            @Override
            public GameEvent getEvent(Sensor self, Body other) {
                fired = true;
                self.dispose();
                return base.getEvent(self, other);
            }
        };
    }
}
