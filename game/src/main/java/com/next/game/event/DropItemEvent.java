package com.next.game.event;

import com.next.engine.event.GameEvent;

public record DropItemEvent(String tag, float x, float y) implements GameEvent {
}
