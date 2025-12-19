package com.next.model;

import com.next.core.data.Mailbox;
import com.next.core.model.Prop;
import com.next.core.physics.CollisionEvent;
import com.next.core.physics.CollisionType;
import com.next.core.physics.CollisionResult;
import com.next.graphics.Layer;
import com.next.graphics.RenderRequest;

public class Spell extends Prop {

    public Spell(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        super(spriteId, worldX, worldY, collisionType);
    }

    @Override
    public CollisionResult onCollision(CollisionEvent event) {
        if (event.collider() instanceof Player player) {
            player.boostSpeed(3);
            this.dispose();
        }
        return super.onCollision(event);
    }

    @Override
    public void onDispose(Mailbox mailbox) {
        super.onDispose(mailbox);
        mailbox.renderQueue.submit(Layer.UI, "Mercury Bless!", -60, -25, RenderRequest.Position.CENTERED, 300);
    }
}
