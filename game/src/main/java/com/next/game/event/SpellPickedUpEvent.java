package com.next.game.event;

import com.next.engine.event.GameEvent;
import com.next.game.model.Player;
import com.next.game.model.Spell;

public record SpellPickedUpEvent(Spell spell, Player player) implements GameEvent {
}
