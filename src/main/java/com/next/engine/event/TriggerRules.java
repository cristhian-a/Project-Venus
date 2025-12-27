package com.next.engine.event;

import com.next.engine.model.Sensor;
import com.next.engine.physics.Body;
import org.jspecify.annotations.NonNull;

public final class TriggerRules {

    private TriggerRules() {}

    public static ConditionBuilder when(Condition condition) {
        return new ConditionBuilder(condition);
    }

    public static Condition and(@NonNull Condition... conditions) {
        return (self, other) -> {
            for (Condition condition : conditions) {
                if (!condition.satisfy(self, other)) return false;
            }
            return true;
        };
    }

    public static Condition or(@NonNull Condition... conditions) {
        return (self, other) -> {
            for (Condition condition : conditions) {
                if (condition.satisfy(self, other)) return true;
            }
            return false;
        };
    }

    public static Condition not(@NonNull Condition condition) {
        return (self, other) -> !condition.satisfy(self, other);
    }

    @FunctionalInterface
    public interface Condition {
        boolean satisfy(Sensor self, Body other);
    }

    @FunctionalInterface
    public interface Action {
        GameEvent create(Sensor self, Body other);
    }

    public static final class ConditionBuilder {
        private Condition condition;

        ConditionBuilder(Condition base) {
            this.condition = base;
        }

        public ConditionBuilder and(@NonNull Condition other) {
            condition = TriggerRules.and(condition, other);
            return this;
        }

        public ConditionBuilder or(@NonNull Condition other) {
            condition = TriggerRules.or(condition, other);
            return this;
        }

        public TriggerRule then(@NonNull Action action) {
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
    }
}
