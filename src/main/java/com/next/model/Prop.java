package com.next.model;

import com.next.core.CollisionType;

public class Prop extends Actor {

    public Prop(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        this.spriteId = spriteId;
        this.collisionType = collisionType;

        collisionBox = new CollisionBox(0, 0, 16, 16);
        setPosition(worldX, worldY);
    }
}
