package com.next.game.model;

import com.next.engine.data.Registry;
import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionType;

public class SilverCoin extends WorldItem {

    private static final String NAME = "Silver Coin";
    private static final String DESCRIPTION = "A silver coin.";

    public int value = 10;

    public SilverCoin(float worldX, float worldY) {
        CollisionType colType = CollisionType.TRIGGER;
        final int sprite = Registry.textureIds.get("silver-coin.png");
        CollisionBox box = new CollisionBox(worldX, worldY, -4, -8, 8, 16);

        super(sprite, NAME, DESCRIPTION, worldX, worldY, null, colType, box);
        this.inventoryVersion = this;
    }
}
