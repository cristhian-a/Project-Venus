package com.next.engine.event;

import com.next.engine.scene.World;

public record WorldTransitionEvent(World world) implements GameEvent {
}
