package com.next.engine.event;

import com.next.engine.model.Sensor;
import com.next.engine.physics.Body;

public final class TriggerRules {

    public static TriggerRule when(Condition condition, Action action) {
        return new TriggerRule() {

            @Override
            public boolean shouldFire(Sensor self, Body other) {
                return condition.satisfy(self, other);
            }

            @Override
            public GameEvent getEvent(Sensor self, Body other) {
                return action.create(self, other);
            }
        };
    }

    @FunctionalInterface
    public interface Condition {
        boolean satisfy(Sensor self, Body other);
    }

    @FunctionalInterface
    public interface Action {
        GameEvent create(Sensor self, Body other);
    }
}
