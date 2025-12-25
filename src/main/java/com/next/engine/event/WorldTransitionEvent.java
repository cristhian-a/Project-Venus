package com.next.engine.event;

import com.next.world.World;

public record WorldTransitionEvent(World world) implements GameEvent {
}
