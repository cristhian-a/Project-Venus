package com.next.engine.event;

import com.next.engine.Conductor;

/**
 * When fired, makes the game loop safely stops after the current update cycle.
 */
public record GracefullyStopEvent() implements GameEvent {

    public static final class Handler {
        private final Conductor conductor;

        public Handler(Conductor conductor) {
            this.conductor = conductor;
        }

        public void onFire(GracefullyStopEvent event) {
            conductor.gracefullyStop();
        }
    }
}
