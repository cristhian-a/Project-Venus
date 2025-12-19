package com.next.event;

import com.next.core.event.GameEvent;
import com.next.model.Key;

public record KeyPickedUpEvent(Key key) implements GameEvent {
}
