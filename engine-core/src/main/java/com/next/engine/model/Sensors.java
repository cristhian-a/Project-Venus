package com.next.engine.model;

import com.next.engine.annotations.internal.Experimental;
import com.next.engine.event.GameEvent;
import com.next.engine.event.TriggerRule;
import com.next.engine.event.TriggerRules;
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

    public static SensorBuilder builder() {
        return new SensorBuilder();
    }

    /**
     * Builder for {@link Sensor}s.
     */
    public static final class SensorBuilder {
        enum Timing { COLLISION, ENTER, EXIT }
        Timing timing;

        TriggerRules.Condition enterCond;
        TriggerRules.Condition exitCond;
        TriggerRules.Condition collisionCond;
        TriggerRules.Action enterAction;
        TriggerRules.Action exitAction;
        TriggerRules.Action collisionAction;

        public SensorBuilder onEnter(TriggerRules.Condition condition) {
            this.enterCond = condition;
            timing = Timing.ENTER;
            return this;
        }

        public SensorBuilder onExit(TriggerRules.Condition condition) {
            this.exitCond = condition;
            timing = Timing.EXIT;
            return this;
        }

        public SensorBuilder onCollision(TriggerRules.Condition condition) {
            this.collisionCond = condition;
            timing = Timing.COLLISION;
            return this;
        }

        public SensorBuilder then(TriggerRules.Action action) {
            switch (timing) {
                case ENTER: enterAction = action; break;
                case EXIT: exitAction = action; break;
                case COLLISION: collisionAction = action; break;
            }
            return this;
        }

        private static TriggerRule ruleFrom(TriggerRules.Condition cond, TriggerRules.Action action) {
            return new TriggerRule() {
                @Override
                public boolean shouldFire(Sensor self, Body other) {
                    return cond.satisfy(self, other);
                }
                @Override
                public GameEvent getEvent(Sensor self, Body other) {
                    return action.create(self, other);
                }
            };
        }

        public @NonNull Sensor build(float x, float y, float width, float height) {
            TriggerRule collision = collisionCond == null ? null : ruleFrom(collisionCond, collisionAction);
            TriggerRule enter = enterCond == null ? null : ruleFrom(enterCond, enterAction);
            TriggerRule exit = exitCond == null ? null : ruleFrom(exitCond, exitAction);

            Sensor s = new Sensor(x, y, width, height, collision);
            s.enterRule = enter;
            s.exitRule = exit;
            return s;
        }
    }
}
