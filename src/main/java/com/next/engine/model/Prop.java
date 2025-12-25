package com.next.engine.model;

import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionType;

public class Prop extends Actor {

    public Prop(int spriteId, float worldX, float worldY, CollisionType collisionType) {
        this.spriteId = spriteId;
        this.collisionType = collisionType;

        this.layer = 1;
        this.collisionMask = 0;

        collisionBox = new CollisionBox(0, 0, 16, 16);
        setPosition(worldX, worldY);
    }

    public Prop(int spriteId, float worldX, float worldY, float mass, CollisionType collisionType,
                float offsetX, float offSetY, float width, float height
    ) {
        this.spriteId = spriteId;
        this.collisionType = collisionType;

        this.layer = 1;
        this.collisionMask = 0;

        collisionBox = new CollisionBox(offsetX, offSetY, width, height);
        setPosition(worldX, worldY);
        this.mass = mass;
    }
}
