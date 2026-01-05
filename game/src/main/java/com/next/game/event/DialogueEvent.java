package com.next.game.event;

import com.next.engine.event.GameEvent;
import com.next.game.model.Npc;
import com.next.game.model.Player;

public record DialogueEvent(Player player, Npc other) implements GameEvent {
}
