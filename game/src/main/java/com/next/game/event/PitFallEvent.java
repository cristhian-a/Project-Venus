package com.next.game.event;

import com.next.engine.event.EventDispatcher;
import com.next.engine.event.GameEvent;
import com.next.engine.model.Sensor;
import com.next.game.model.Player;

public record PitFallEvent(Sensor sensor, Player player) implements GameEvent {

    public static final class Handler {

        public Handler(EventDispatcher dispatcher) {
            dispatcher.register(PitFallEvent.class, this::onFire);
        }

        public void onFire(PitFallEvent event) {
            event.player().setHealth(event.player().getHealth() - 1);
        }
    }
}
