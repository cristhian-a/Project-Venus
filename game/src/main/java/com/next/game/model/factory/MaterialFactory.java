package com.next.game.model.factory;

import com.next.engine.data.Registry;
import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionType;
import com.next.game.model.MaterialBasic;
import com.next.game.model.ItemSilverCoin;
import com.next.game.model.WorldItem;

public class MaterialFactory {

    public static MaterialBasic create(int spriteId, String name, String description) {
        return new MaterialBasic(spriteId, name, description);
    }

    public static MaterialBasic woodLogMaterial() {
        final int sprite = Registry.textureIds.get("wood_log.png");
        final String name = "Wood Log";
        final String description = "A log made from wood";
        return create(sprite, name, description);
    }

    public static WorldItem woodLogWorldItem(float x, float y) {
        final int sprite = Registry.textureIds.get("wood_log.png");
        final String name = "Wood Log";
        final String description = "A log made from wood";
        CollisionType collisionType = CollisionType.SOLID;
        CollisionBox box = new CollisionBox(x, y, -4, -4, 8, 12);

        return new WorldItem(sprite, name, description, x, y, woodLogMaterial(), collisionType, box);
    }

    public static MaterialBasic purpleEssenceMaterial() {
        final int sprite = Registry.textureIds.get("purple-essence.png");
        final String name = "Purple Essence";
        final String description = "A weird liquid extracted from \nmonsters that can be used to \ncraft spells";
        return create(sprite, name, description);
    }

    public static WorldItem purpleEssenceWorldItem(float x, float y) {
        final int sprite = Registry.textureIds.get("purple-essence.png");
        final String name = "Purple Essence";
        final String description = "A weird liquid extracted from \nmonsters that can be used to \ncraft spells";
        CollisionType collisionType = CollisionType.TRIGGER;
        CollisionBox box = new CollisionBox(x, y, -4, -4, 8, 8);

        return new WorldItem(sprite, name, description, x, y, purpleEssenceMaterial(), collisionType, box);
    }

    public static ItemSilverCoin silverCoin(float x, float y) {
        return new ItemSilverCoin(x, y);
    }

}
