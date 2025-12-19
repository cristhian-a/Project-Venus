package com.next.model;

import com.next.core.data.Mailbox;
import com.next.core.model.Prop;
import com.next.core.physics.CollisionEvent;
import com.next.core.physics.CollisionType;
import com.next.core.physics.CollisionResult;
import com.next.graphics.Layer;
import com.next.graphics.RenderRequest;

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
            return new CollisionResult(CollisionResult.Type.TRIGGER, event.collider(), 0, 0);
        }

        return super.onCollision(event);
    }

    @Override
    public void submitRender(Mailbox mailbox) {
        super.submitRender(mailbox);
    }

    @Override
    public void onDispose(Mailbox mailbox) {
        mailbox.renderQueue.submit(Layer.UI, "Got a Key!", -32, -25, RenderRequest.Position.CENTERED, 240);
    }
}
