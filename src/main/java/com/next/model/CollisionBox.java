package com.next.model;

import lombok.Getter;
import lombok.Setter;

public class CollisionBox {
    @Getter private final AABB bounds;
    protected final float offsetX;
    protected final float offsetY;

    @Getter @Setter private boolean solid;
    @Getter @Setter private boolean colliding;

    public CollisionBox(float offsetX, float offsetY, float width, float height) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        bounds = new AABB(0, 0, width, height);
    }

    public void update(float x, float y) {
        bounds.x = x + offsetX;
        bounds.y = y + offsetY;
    }

    public boolean intersects(CollisionBox other) {
        return this.bounds.intersects(other.bounds);
    }
}
