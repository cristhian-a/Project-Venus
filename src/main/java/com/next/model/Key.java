package com.next.model;

import com.next.engine.model.Prop;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionCollector;
import com.next.engine.physics.CollisionType;
import com.next.event.KeyPickedUpEvent;

public class Key extends Prop {

    public Key(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        super(spriteId, worldX, worldY, collisionType);
    }

    @Override
    public void onCollision(Body other, CollisionCollector collector) {
        if (other instanceof Player player) {
            collector.post(() -> new KeyPickedUpEvent(this, player));
        }
    }

}
