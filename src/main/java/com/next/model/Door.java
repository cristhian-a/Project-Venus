package com.next.model;

import com.next.core.CollisionType;

public class Door extends Prop {
    public Door(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        super(spriteId, worldX, worldY, collisionType);
    }

    @Override
    public void onTrigger(Actor other) {
        this.discard();
    }
}
