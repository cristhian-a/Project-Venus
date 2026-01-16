package com.next.engine.model;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class Prop extends Entity implements Body, Renderable {
    protected int spriteId;
    protected int layer;
    protected int collisionMask;
    protected int lastQueryId;
    protected CollisionBox collisionBox;
    protected CollisionType collisionType;

    public Prop(int spriteId, float worldX, float worldY, int layer, CollisionType collisionType, CollisionBox collisionBox) {
        this.spriteId = spriteId;
        this.layer = layer;
        this.worldX = worldX;
        this.worldY = worldY;
        this.collisionType = collisionType;
        this.collisionBox = collisionBox;
        this.lastQueryId = -1;
    }

    @Override
    public float getX() {
        return worldX;
    }

    @Override
    public float getY() {
        return worldY;
    }

    @Override
    public int getLayer() {
        return layer;
    }

    @Override
    public int getCollisionMask() {
        return collisionMask;
    }

    @Override
    public CollisionBox getCollisionBox() {
        return collisionBox;
    }

    @Override
    public CollisionType getCollisionType() {
        return collisionType;
    }

    @Override
    public void setPosition(float x, float y) {
        worldX = x;
        worldY = y;
        if (collisionBox != null)
            collisionBox.update(worldX, worldY);
    }

    @Override
    public int getLastQueryId() {
        return lastQueryId;
    }

    @Override
    public void setLastQueryId(int id) {
        this.lastQueryId = id;
    }

    @Override
    public void collectRender(RenderQueue queue) {
        queue.submit(Layer.ACTORS, (int) worldX, (int) worldY, spriteId);
    }
}
