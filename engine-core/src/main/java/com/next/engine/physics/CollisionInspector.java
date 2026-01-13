package com.next.engine.physics;

import com.next.engine.scene.Scene;
import lombok.Getter;

/**
 * Detects collisions between actors and the world. {@link CollisionInspector#inspecting(Scene)} must be called to set
 * which world is being inspected.
 */
class CollisionInspector {

    @Getter private Scene scene;
    private int tileSize;

    public CollisionInspector() {
    }

    public void inspecting(Scene scene) {
        this.scene = scene;
        this.tileSize = scene.world.getTileSize();
    }

    public boolean isCollidingWithTile(Body agent) {
        AABB box = agent.getCollisionBox().getBounds();

        // EPSILON is required to adjust right and bottom sides to not collide prematurely
        // in relation to the left and top sides.
        // This is needed because the rendering occurs left to right, top to bottom (as far as I get it)
        final float EPSILON = 0.0001f;

        // grid coordinates (columns)
        int left    = (int) Math.floor(box.x / tileSize);
        int right   = (int) Math.floor((box.x + box.width - EPSILON) / tileSize);
        int top     = (int) Math.floor(box.y / tileSize);
        int bottom  = (int) Math.floor((box.y + box.height - EPSILON) / tileSize);

        for (int row = top; row <= bottom; row++) {
            for (int col = left; col <= right; col++) {
                if (scene.world.makesContact(row, col, agent)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isColliding(Body actor, Body other) {
        if ((actor.getLayer() & other.getCollisionMask()) == 0 && (other.getLayer() & actor.getCollisionMask()) == 0)
            return false;
        return actor.getCollisionBox().intersects(other.getCollisionBox());
    }

}
