package com.next.engine.physics;

import lombok.Getter;

public class CollisionBox {
    @Getter private final AABB bounds;
    @Getter protected final float offsetX;
    @Getter protected final float offsetY;

    public CollisionBox(float offsetX, float offsetY, float width, float height) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        bounds = new AABB(0, 0, width, height);
    }

    public CollisionBox(float x, float y, float offsetX, float offsetY, float width, float height) {
        this(offsetX, offsetY, width, height);
        bounds.x = x + offsetX;
        bounds.y = y + offsetY;
    }

    public void update(float x, float y) {
        bounds.x = x + offsetX;
        bounds.y = y + offsetY;
    }

    public boolean intersects(CollisionBox other) {
        return this.bounds.intersects(other.bounds);
    }
}
