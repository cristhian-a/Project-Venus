package com.next.game.event;

import com.next.engine.event.GameEvent;
import com.next.engine.model.HitboxSpec;
import com.next.game.model.Combatant;
import com.next.game.model.Damageable;

public record StrikeEvent(Combatant striker, Damageable target, HitboxSpec spec) implements GameEvent {
}
