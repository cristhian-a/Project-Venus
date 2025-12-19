package com.next.event;

import com.next.core.event.GameEvent;
import com.next.model.Player;
import com.next.model.Spell;

public record SpellPickedUpEvent(Spell spell, Player player) implements GameEvent {
}
