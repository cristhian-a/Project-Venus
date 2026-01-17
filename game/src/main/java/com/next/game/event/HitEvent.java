package com.next.game.event;

import com.next.engine.event.GameEvent;
import com.next.engine.model.Entity;
import com.next.engine.model.HitboxSpec;
import com.next.game.model.Hittable;

public record HitEvent(Entity striker, Hittable target, HitboxSpec spec) implements GameEvent {
}
