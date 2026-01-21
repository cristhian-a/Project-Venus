package com.next.game.model;

import com.next.engine.event.EventCollector;
import com.next.engine.model.Prop;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionType;
import com.next.game.event.ItemPickedUpEvent;
import com.next.game.rules.Layers;
import lombok.Getter;

public class WorldItem extends Prop implements Item {

    protected final String name;
    protected final String description;
    @Getter protected Item inventoryVersion;

    public WorldItem(int spriteId, String name, String description,
                     float worldX, float worldY,
                     Item inventoryVersion,
                     CollisionType collisionType, CollisionBox collisionBox
    ) {
        this.name = name;
        this.description = description;
        this.inventoryVersion = inventoryVersion;
        var layer = Layers.ITEM;
        super(spriteId, worldX, worldY, layer, collisionType, collisionBox);
    }

    @Override
    public int getIcon() {
        return this.spriteId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getInfo() {
        return description;
    }

    @Override
    public void onEnter(Body other, EventCollector collector) {
        if (other instanceof Player p)
            collector.post(() -> new ItemPickedUpEvent(this, p));
    }
}
