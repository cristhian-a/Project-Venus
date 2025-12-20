package com.next.event;

import com.next.engine.event.GameEvent;
import com.next.model.Key;
import com.next.model.Player;

public record KeyPickedUpEvent(Key key, Player player) implements GameEvent {
}
