package com.next.engine.event;

public record ExitEvent() implements GameEvent {

    public static final class Handler {
        public void onFire(ExitEvent event) {
            System.exit(0);
        }
    }
}
