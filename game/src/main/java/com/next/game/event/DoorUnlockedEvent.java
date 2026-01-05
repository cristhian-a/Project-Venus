package com.next.game.event;

import com.next.engine.event.GameEvent;
import com.next.game.model.Door;
import com.next.game.model.Player;

public record DoorUnlockedEvent(Door door, Player player) implements GameEvent {
}
