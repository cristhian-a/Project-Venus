package com.next.game.event;

import com.next.engine.event.GameEvent;
import com.next.game.model.Mob;

public record MobDeathEvent(Mob mob) implements GameEvent {
}
