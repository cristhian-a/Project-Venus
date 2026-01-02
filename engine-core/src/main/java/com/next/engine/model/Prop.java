package com.next.engine.model;

import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionCollector;
import com.next.engine.physics.CollisionType;

public class Prop extends Actor {

    public Prop(int spriteId, float worldX, float worldY, CollisionType collisionType, CollisionBox collisionBox) {
        this.spriteId = spriteId;
        this.collisionType = collisionType;

        this.layer = 1;
        this.collisionMask = 0;

        this.collisionBox = collisionBox;
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

    @Override
    public void onCollision(Body other, CollisionCollector collector) {
    }
}
