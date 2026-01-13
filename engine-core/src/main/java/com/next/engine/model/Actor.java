package com.next.engine.model;

import com.next.engine.data.Mailbox;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.physics.*;
import com.next.engine.graphics.Layer;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

@Getter
public abstract class Actor extends Entity implements Body {
    protected int spriteId;

    protected int layer;
    protected int collisionMask;
    protected CollisionBox collisionBox;        // TODO: remember to initialize it anytime (or move it to children)
    protected CollisionType collisionType = CollisionType.NONE;

    @Setter public int lastQueryId = -1;

    // TODO shall be removed once I figure a place to put the mass related physics properties
    protected float mass = 1f;
    public float vx;
    public float vy;

    @Deprecated
    public void setVelocity(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }

    @Deprecated
    public boolean isImmovable() {
        return mass <= 0f;
    }

    @Deprecated
    public float invMass() {
        return isImmovable() ? 0f : 1f / mass;
    }

    public void update(double delta) {
    }

    public void submitRender(@NonNull RenderQueue queue) {
        queue.submit(Layer.ACTORS, (int) worldX, (int) worldY, spriteId);
    }

    @Override
    public void setPosition(float worldX, float worldY) {
        this.worldX = worldX;
        this.worldY = worldY;
        if (collisionBox != null) {
            collisionBox.update(worldX, worldY);
        }
    }

    /**
     * Should be called when effectively discarded. By default, it does nothing. Override it whenever any behavior upon
     * disposing is needed.
     */
    @Override
    public void onDispose() {}

    @Override
    public float getX() {
        return worldX;
    }

    @Override
    public float getY() {
        return worldY;
    }
}
