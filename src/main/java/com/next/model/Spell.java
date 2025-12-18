package com.next.model;

import com.next.core.physics.CollisionEvent;
import com.next.core.physics.CollisionType;
import com.next.core.physics.CollisionResult;

public class Spell extends Prop {

    public Spell(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        super(spriteId, worldX, worldY, collisionType);
    }

    @Override
    public CollisionResult onCollision(CollisionEvent event) {
        if (event.collider() instanceof Player player) {
            player.boostSpeed(2);
            this.dispose();
        }
        return super.onCollision(event);
    }
}
