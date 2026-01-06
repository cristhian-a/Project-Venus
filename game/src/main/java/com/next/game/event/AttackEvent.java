package com.next.game.event;

import com.next.engine.event.GameEvent;
import com.next.engine.model.HitboxSpec;
import com.next.game.model.Combatant;

public record AttackEvent(Combatant striker, Combatant target, HitboxSpec spec) implements GameEvent {
}
