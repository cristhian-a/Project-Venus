package com.next.model;

import com.next.core.model.Prop;
import com.next.core.physics.CollisionEvent;
import com.next.core.physics.CollisionType;
import com.next.core.physics.CollisionResult;
import com.next.event.FinishGameEvent;

import java.util.List;

public class Chest extends Prop {

    public Chest(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        super(spriteId, worldX, worldY, collisionType);
    }

    @Override
    public CollisionResult onCollision(CollisionEvent event) {
        return new CollisionResult(
                this.collisionType, event.collider(), 0, 0,
                FinishGameEvent::new
        );
    }
}
