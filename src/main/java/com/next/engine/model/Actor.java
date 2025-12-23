package com.next.engine.model;

import com.next.engine.data.Mailbox;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionEvent;
import com.next.engine.physics.CollisionType;
import com.next.engine.physics.CollisionResult;
import com.next.engine.graphics.Layer;
import com.next.engine.system.Debugger;
import lombok.Getter;
import lombok.Setter;

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
    public int lastQueryId = -1;
    @Setter protected int id;   // TODO check how this should be implemented

    public void update(double delta, Mailbox mailbox) {
    }

    public void submitRender(RenderQueue queue) {
        if (collisionBox != null) Debugger.publish("ACTOR BOX" + this, collisionBox);
        queue.submit(Layer.ACTORS, (int) worldX, (int) worldY, spriteId);
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
        return new CollisionResult(
                collisionType,
                event.collider(),
                0,
                0,
                null
        );
    }

    /**
     * Marks this actor for disposal, but does not dispose it immediately.
     */
    public final void dispose() {
        this.disposed = true;
    }

    /**
     * Should be called when effectively discarded. By default, it does nothing. Override it whenever any behavior upon
     * disposing is needed.
     */
    public void onDispose() {}
}
