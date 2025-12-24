package com.next.model;

import com.next.engine.model.Actor;
import com.next.engine.model.Prop;
import com.next.engine.physics.CollisionType;
import com.next.engine.physics.CollisionResult;
import com.next.event.SpellPickedUpEvent;

public class Spell extends Prop {

    public Spell(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        super(spriteId, worldX, worldY, collisionType);
    }

    @Override
    public CollisionResult onCollision(Actor other) {
        if (other instanceof Player player) {
            return new CollisionResult(
                    () -> new SpellPickedUpEvent(this, player)
            );
        }
        return super.onCollision(other);
    }

}
