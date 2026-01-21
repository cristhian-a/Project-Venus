package com.next.game.event;

import com.next.engine.event.GameEvent;
import com.next.engine.model.HitboxSpec;
import com.next.game.model.Combatant;
import com.next.game.model.DestructibleTile;

public record TileHitEvent(Combatant striker, DestructibleTile tile, HitboxSpec spec) implements GameEvent {
}
