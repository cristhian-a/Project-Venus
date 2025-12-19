package com.next.core.model;

import com.next.core.data.Mailbox;
import com.next.core.physics.CollisionBox;
import com.next.core.physics.CollisionEvent;
import com.next.core.physics.CollisionType;
import com.next.core.physics.CollisionResult;
import com.next.graphics.Layer;
import com.next.system.Debugger;
import lombok.Getter;

@Getter
public abstract class Actor {
    protected int spriteId;
    protected float worldX;
    protected float worldY;

    protected int layer;
    protected int collisionMask;
    protected CollisionBox collisionBox;        // TODO: remember to initialize it anytime (or move it to children)
    protected CollisionType collisionType = CollisionType.NONE;

    protected boolean disposed = false;

    public void update(double delta, Mailbox mailbox) {
    }

    public void submitRender(Mailbox mailbox) {
        if (collisionBox != null) Debugger.publishCollision("ACTOR BOX" + this, collisionBox);
        mailbox.renderQueue.submit(Layer.ACTORS, (int) worldX, (int) worldY, spriteId);
    }

    public void setPosition(float worldX, float worldY) {
        this.worldX = worldX;
        this.worldY = worldY;
        if (collisionBox != null) {
            collisionBox.update(worldX, worldY);
        }
    }

    public void moveX(float dx) {
        if (dx == 0) return;

        worldX += dx;
        collisionBox.update(worldX, worldY);
    }

    public void moveY(float dy) {
        if (dy == 0) return;

        worldY += dy;
        collisionBox.update(worldX, worldY);
    }

    public CollisionResult onCollision(CollisionEvent event) {
        return null;
    }

    public void dispose() {
        this.disposed = true;
    }

    public void onDispose(Mailbox mailbox) {}
}
