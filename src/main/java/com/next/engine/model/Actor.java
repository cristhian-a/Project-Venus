package com.next.engine.model;

import com.next.engine.data.Mailbox;
import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionEvent;
import com.next.engine.physics.CollisionType;
import com.next.engine.physics.CollisionResult;
import com.next.engine.graphics.Layer;
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

    public final void dispose() {
        this.disposed = true;
        onDispose();
    }

    /**
     * Always called after {@code dispose()}. By default, it does nothing. Override it with any behavior upon disposing
     * is needed.
     */
    public void onDispose() {}
}
