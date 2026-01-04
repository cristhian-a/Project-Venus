package com.next.event;

import com.next.engine.event.GameEvent;

public record UiDamageEvent(int entityId) implements GameEvent {
}
