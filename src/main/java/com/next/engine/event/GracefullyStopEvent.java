package com.next.engine.event;

import com.next.Loop;

/**
 * When fired, makes the game loop safely stops after the current update cycle.
 */
public record GracefullyStopEvent() implements GameEvent {

    public static final class Handler {
        private final Loop loop;

        public Handler(Loop loop) {
            this.loop = loop;
        }

        public void onFire(GracefullyStopEvent event) {
            loop.gracefullyStop();
        }
    }
}
