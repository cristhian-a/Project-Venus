package com.next.model;

import com.next.core.physics.CollisionBox;
import com.next.core.physics.CollisionType;

public class Prop extends Actor {

    public Prop(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        this.spriteId = spriteId;
        this.collisionType = collisionType;

        this.layer = 1;
        this.collisionMask = 0;

        collisionBox = new CollisionBox(0, 0, 16, 16);
        setPosition(worldX, worldY);
    }
}
