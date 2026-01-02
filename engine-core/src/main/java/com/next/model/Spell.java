package com.next.model;

import com.next.engine.model.Prop;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionCollector;
import com.next.engine.physics.CollisionType;
import com.next.event.SpellPickedUpEvent;

public class Spell extends Prop {

    public Spell(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        CollisionBox box = new CollisionBox(worldX, worldY, -8, -8, 16, 16);
        super(spriteId, worldX, worldY, collisionType, box);
    }

    @Override
    public void onCollision(Body other, CollisionCollector collector) {
        if (other instanceof Player player) {
            collector.post(() -> new SpellPickedUpEvent(this, player));
        }
    }

}
