package com.next.engine.model;

import com.next.engine.event.TriggerRule;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionResult;
import com.next.engine.physics.CollisionType;
import lombok.Getter;

public class Sensor extends Entity implements Body {

    protected float worldX;
    protected float worldY;

    protected int layer = 1;
    protected int collisionMask = 2;
    protected int lastQueryId = -1;
    protected final CollisionBox collisionBox;

    protected final TriggerRule rule;
    @Getter protected boolean active = true;

    public Sensor(float worldX, float worldY, float width, float height, TriggerRule rule) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.rule = rule;

        collisionBox = new CollisionBox(worldX, worldY, 0, 0, width, height);
    }

    @Override
    public CollisionResult onCollision(Body other) {
        if (active && rule.shouldFire(this, other))
            return new CollisionResult(() -> rule.getEvent(this, other));

        return null;
    }

    @Override
    public void onDispose() {
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
        return CollisionType.TRIGGER;
    }

    @Override
    public void setPosition(float x, float y) {
        worldX = x;
        worldY = y;
        collisionBox.update(worldX, worldY);
    }

    @Override
    public int getLastQueryId() {
        return lastQueryId;
    }

    @Override
    public void setLastQueryId(int id) {
        lastQueryId = id;
    }

    public void toggle() {
        active = !active;
    }

}
