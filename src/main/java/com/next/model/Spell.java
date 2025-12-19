package com.next.model;

import com.next.core.data.Mailbox;
import com.next.core.model.Prop;
import com.next.core.physics.CollisionEvent;
import com.next.core.physics.CollisionType;
import com.next.core.physics.CollisionResult;
import com.next.event.SpellPickedUpEvent;
import com.next.graphics.Layer;
import com.next.graphics.RenderRequest;

import java.util.List;

public class Spell extends Prop {

    public Spell(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        super(spriteId, worldX, worldY, collisionType);
    }

    @Override
    public CollisionResult onCollision(CollisionEvent event) {
        if (event.collider() instanceof Player player) {
            return new CollisionResult(
                    CollisionResult.Type.TRIGGER,
                    event.collider(),
                    0,
                    0,
                    List.of(new SpellPickedUpEvent(this, player))
            );
        }
        return super.onCollision(event);
    }

}
