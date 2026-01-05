package com.next.game.event;

import com.next.engine.event.GameEvent;
import com.next.game.model.Player;

public record LevelUpEvent(Player player) implements GameEvent {
}
