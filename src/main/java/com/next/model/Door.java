package com.next.model;

import com.next.core.model.Prop;
import com.next.core.physics.CollisionEvent;
import com.next.core.physics.CollisionType;
import com.next.core.physics.CollisionResult;
import com.next.event.NoKeysEvent;
import com.next.event.DoorUnlockedEvent;

import java.util.List;

public class Door extends Prop {

    public Door(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        super(spriteId, worldX, worldY, collisionType);
    }

    @Override
    public CollisionResult onCollision(CollisionEvent event) {
        if (event.collider() instanceof Player player) {
            if (!player.getHeldKeys().isEmpty()) {
                return new CollisionResult(
                        this.collisionType,
                        event.collider(),
                        0,
                        0,
                        () -> new DoorUnlockedEvent(this, player)
                );
            } else {
                return new CollisionResult(
                        this.collisionType,
                        event.collider(),
                        0,
                        0,
                        NoKeysEvent::new
                );
            }
        }
        return new CollisionResult(this.collisionType, event.collider(), 0, 0, null);
    }
}
