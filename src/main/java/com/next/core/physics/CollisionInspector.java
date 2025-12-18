package com.next.core.physics;

import com.next.model.Actor;
import com.next.world.Scene;
import lombok.Getter;

public class CollisionInspector {

    @Getter private Scene scene;
    private int tileSize;

    public CollisionInspector() {
    }

    public void inspecting(Scene scene) {
        this.scene = scene;
        this.tileSize = scene.world.getTileSize();
    }

    public boolean isCollidingWithTile(Actor actor) {
        AABB box = actor.getCollisionBox().getBounds();

        final float EPSILON = 0.0001f;  // Needed to adjust right and bottom sides to not collide prematurely
                                        // in relation to left and top sides.
                                        // This is needed because the rendering occurs right to left, top to bottom

        // grid coordinates (columns)
        int left    = (int) Math.floor(box.x / tileSize);
        int right   = (int) Math.floor((box.x + box.width - EPSILON) / tileSize);
        int top     = (int) Math.floor(box.y / tileSize);
        int bottom  = (int) Math.floor((box.y + box.height - EPSILON) / tileSize);

        for (int row = top; row <= bottom; row++) {
            for (int col = left; col <= right; col++) {
                if (scene.world.isSolid(row, col)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isColliding(Actor actor, Actor other) {
        if ((actor.getLayer() & other.getCollisionMask()) == 0 && (other.getLayer() & actor.getCollisionMask()) == 0)
            return false;
        return actor.getCollisionBox().intersects(other.getCollisionBox());
    }

}
