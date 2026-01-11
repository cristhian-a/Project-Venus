package com.next.game.model;

import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionType;

public class Key extends WorldItem {

    private static final String NAME = "Key";
    private static final String DESCRIPTION = "Use it to open doors.";

    public Key(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        CollisionBox box = new CollisionBox(worldX, worldY, -8, -8, 16, 16);
        super(spriteId, NAME, DESCRIPTION, worldX, worldY, null, collisionType, box);
        this.inventoryVersion = this;
    }

}
