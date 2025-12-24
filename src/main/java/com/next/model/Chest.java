package com.next.model;

import com.next.engine.model.Actor;
import com.next.engine.model.Prop;
import com.next.engine.physics.CollisionType;
import com.next.engine.physics.CollisionResult;
import com.next.event.FinishGameEvent;

public class Chest extends Prop {

    public Chest(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        super(spriteId, worldX, worldY, collisionType);
    }

    @Override
    public CollisionResult onCollision(Actor other) {
        return new CollisionResult(FinishGameEvent::new);
    }
}
