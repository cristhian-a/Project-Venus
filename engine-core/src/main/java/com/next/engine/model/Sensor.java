package com.next.engine.model;

import com.next.engine.event.EventCollector;
import com.next.engine.event.TriggerRule;
import com.next.engine.physics.*;

public class Sensor extends Entity implements Body, Updatable {

    protected float worldX;
    protected float worldY;

    protected int layer;
    protected int collisionMask;
    protected int lastQueryId = -1;
    protected CollisionBox collisionBox;

    protected TriggerRule onCollision;
    protected TriggerRule enterRule;
    protected TriggerRule exitRule;

    public Sensor() {
    }

    public Sensor(float worldX, float worldY, float width, float height, TriggerRule onCollision) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.onCollision = onCollision;

        collisionBox = new CollisionBox(worldX, worldY, 0, 0, width, height);
    }

    @Override
    public void onCollision(Body other, EventCollector collector) {
        if (onCollision != null && onCollision.shouldFire(this, other))
            collector.post(() -> onCollision.getEvent(this, other));
    }

    @Override
    public void onEnter(Body other, EventCollector collector) {
        if (enterRule != null && enterRule.shouldFire(this, other)) {
            collector.post(() -> enterRule.getEvent(this, other));
        }
    }

    @Override
    public void onExit(Body other, EventCollector collector) {
        if (exitRule != null && exitRule.shouldFire(this, other)) {
            collector.post(() -> exitRule.getEvent(this, other));
        }
    }

    @Override
    public void update(double delta) {
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

}
