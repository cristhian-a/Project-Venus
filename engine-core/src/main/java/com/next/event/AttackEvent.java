package com.next.event;

import com.next.engine.event.GameEvent;
import com.next.model.Combatant;

public record AttackEvent(Combatant striker, Combatant target, int damage) implements GameEvent {
}
