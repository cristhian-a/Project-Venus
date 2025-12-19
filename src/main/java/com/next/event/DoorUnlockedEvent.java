package com.next.event;

import com.next.core.event.GameEvent;
import com.next.model.Door;
import com.next.model.Player;

public record DoorUnlockedEvent(Door door, Player player) implements GameEvent {
}
