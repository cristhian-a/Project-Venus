package com.next.model;

import com.next.engine.model.Prop;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionCollector;
import com.next.engine.physics.CollisionType;
import com.next.event.NoKeysEvent;
import com.next.event.DoorUnlockedEvent;

public class Door extends Prop {

    public Door(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        super(spriteId, worldX, worldY, collisionType);
    }

    @Override
    public void onCollision(Body other, CollisionCollector collector) {
        if (other instanceof Player player) {
            if (!player.getHeldKeys().isEmpty()) {
                collector.post( () -> new DoorUnlockedEvent(this, player));
            } else {
                collector.post(NoKeysEvent::new);
            }
        }
    }
}
