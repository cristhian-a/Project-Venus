package com.next.model;

public class Prop extends Actor {

    public Prop(int spriteId, int worldX, int worldY, boolean solid) {
        this.spriteId = spriteId;

        collisionBox = new CollisionBox(0, 0, 16, 16);
        collisionBox.setSolid(solid);
        setPosition(worldX, worldY);
    }
}
