package com.next.engine.physics;

import com.next.engine.scene.Scene;

/**
 * Detects collisions between entities and the world.
 */
final class CollisionInspector {

    public boolean isCollidingWithTile(final Body agent, final Scene scene) {
        final AABB box = agent.getCollisionBox().getBounds();
        final int tileSize = scene.world.getTileSize();

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

    public boolean isColliding(final Body actor, final Body other) {
        if ((actor.getLayer() & other.getCollisionMask()) == 0 && (other.getLayer() & actor.getCollisionMask()) == 0)
            return false;
        return actor.getCollisionBox().intersects(other.getCollisionBox());
    }

}
