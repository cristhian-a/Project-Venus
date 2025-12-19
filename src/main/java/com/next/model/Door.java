package com.next.model;

import com.next.core.model.Prop;
import com.next.core.physics.CollisionEvent;
import com.next.core.physics.CollisionType;
import com.next.core.physics.CollisionResult;

public class Door extends Prop {

    public Door(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        super(spriteId, worldX, worldY, collisionType);
    }

    @Override
    public CollisionResult onCollision(CollisionEvent event) {
        if (event.collider() instanceof Player player) {
            if (!player.getHeldKeys().isEmpty()) {
                player.getHeldKeys().removeLast();
                IO.println("AAAAAAI CHAVES: " + player.getHeldKeys().size());
                this.dispose();
                return new CollisionResult(CollisionResult.Type.TRIGGER, event.collider(), 0, 0);
            }
        }
        return new CollisionResult(CollisionResult.Type.BLOCK, event.collider(), 0, 0);
    }
}
