package com.next.game.model;

import com.next.engine.model.Prop;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionBox;
import com.next.engine.event.EventCollector;
import com.next.engine.physics.CollisionType;
import com.next.game.event.SpellPickedUpEvent;
import com.next.game.rules.Layers;

public class Spell extends Prop {

    public Spell(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        CollisionBox box = new CollisionBox(worldX, worldY, -8, -8, 16, 16);
        super(spriteId, worldX, worldY, collisionType, box);
        this.layer = Layers.ITEM;
    }

    @Override
    public void onCollision(Body other, EventCollector collector) {
        if (other instanceof Player player) {
            collector.post(() -> new SpellPickedUpEvent(this, player));
        }
    }

}
