package com.next.event;

import com.next.engine.event.GameEvent;
import com.next.model.Door;
import com.next.model.Player;

public record DoorUnlockedEvent(Door door, Player player) implements GameEvent {
}
