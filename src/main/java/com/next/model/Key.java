package com.next.model;

import com.next.core.event.GameEvent;
import com.next.core.data.Mailbox;
import com.next.core.model.Prop;
import com.next.core.physics.CollisionEvent;
import com.next.core.physics.CollisionType;
import com.next.core.physics.CollisionResult;
import com.next.event.KeyPickedUpEvent;

import java.util.List;

public class Key extends Prop {

    public Key(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        super(spriteId, worldX, worldY, collisionType);
    }

    @Override
    public CollisionResult onCollision(CollisionEvent event) {
        if (event.collider() instanceof Player player) {
            player.getHeldKeys().add(this);
            IO.println("AAAAAAI CHAVES: " + player.getHeldKeys().size());
            this.dispose();
            return new CollisionResult(
                    this.collisionType,
                    event.collider(),
                    0,
                    0,
                    () -> new KeyPickedUpEvent(this)
            );
        }

        return super.onCollision(event);
    }

}
