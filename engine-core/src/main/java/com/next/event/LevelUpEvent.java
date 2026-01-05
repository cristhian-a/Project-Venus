package com.next.event;

import com.next.engine.event.GameEvent;
import com.next.model.Player;

public record LevelUpEvent(Player player) implements GameEvent {
}
