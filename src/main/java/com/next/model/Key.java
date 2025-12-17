package com.next.model;

import com.next.core.CollisionType;

public class Key extends Prop {
    public Key(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        super(spriteId, worldX, worldY, collisionType);
    }

    @Override
    public void onTrigger(Actor other) {
        if (other instanceof Player) {
            ((Player) other).getKeys().add(this);
            this.discard();
        }
    }
}
