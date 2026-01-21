package com.next.game.event;

import com.next.engine.event.GameEvent;
import com.next.game.model.Player;
import com.next.game.model.WorldItem;

public record ItemPickedUpEvent(WorldItem item, Player player) implements GameEvent {
}
