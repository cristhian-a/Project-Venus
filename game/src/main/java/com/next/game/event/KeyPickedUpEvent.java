package com.next.game.event;

import com.next.engine.event.GameEvent;
import com.next.game.model.Key;
import com.next.game.model.Player;

public record KeyPickedUpEvent(Key key, Player player) implements GameEvent {
}
