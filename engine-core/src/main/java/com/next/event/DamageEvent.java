package com.next.event;

import com.next.engine.event.EventDispatcher;
import com.next.engine.event.GameEvent;
import com.next.model.Player;

public record DamageEvent(Player player, int damage) implements GameEvent {

    public static final class Handler {

        public Handler(EventDispatcher dispatcher) {
            dispatcher.register(DamageEvent.class, this::onFire);
        }

        public void onFire(DamageEvent event) {
            int damage = event.damage();
            event.player().takeDamage(damage);
        }
    }
}
