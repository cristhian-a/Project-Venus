package com.next.engine.model;

import lombok.Getter;
import lombok.Setter;

public class Light extends Entity {

    @Getter protected final int textureId;
    @Getter @Setter protected float radius;
    @Getter @Setter protected float intensity;

    public Light(int textureId, float worldX, float worldY, float radius, float intensity) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.radius = radius;
        this.intensity = intensity;
        this.textureId = textureId;
    }

    public void setPosition(float x, float y) {
        worldX = x;
        worldY = y;
    }

    /**
     * Adds dx to the world x of the object.
     * @param dx delta movement
     * @param delta delta time
     */
    public void moveX(float dx, double delta) {
        if (dx == 0) return;
        dx += worldX;
        setPosition(dx, worldY);
    }

    /**
     * Adds dy to the world y of the object.
     * @param dy delta movement
     * @param delta delta time
     */
    public void moveY(float dy, double delta) {
        if (dy == 0) return;
        dy += worldY;
        setPosition(worldX, dy);
    }
}
