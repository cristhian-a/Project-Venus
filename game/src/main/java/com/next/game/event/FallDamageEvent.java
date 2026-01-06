package com.next.game.event;

import com.next.engine.event.EventDispatcher;
import com.next.engine.event.GameEvent;
import com.next.game.model.Player;

public record FallDamageEvent(Player player, int damage) implements GameEvent {

    public static final class Handler {

        public Handler(EventDispatcher dispatcher) {
            dispatcher.register(FallDamageEvent.class, this::onFire);
        }

        public void onFire(FallDamageEvent event) {
            int damage = event.damage();
            event.player().takeDamage(damage);
        }
    }
}
