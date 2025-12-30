package com.next.event;

import com.next.engine.event.GameEvent;
import com.next.model.Npc;
import com.next.model.Player;

public record DialogueEvent(Player player, Npc other) implements GameEvent {
}
