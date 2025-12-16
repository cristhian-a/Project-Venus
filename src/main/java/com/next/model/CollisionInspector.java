package com.next.model;

import com.next.system.Debugger;
import lombok.Setter;

public class CollisionInspector {

    private final int TILE_SIZE;
    @Setter private World world;

    public CollisionInspector(int tileSize, World world) {
        TILE_SIZE = tileSize;
        this.world = world;
    }

    public int getTileSize() {
        return TILE_SIZE;
    }

    public boolean isCollidingWithTile(Actor actor) {
        AABB box = actor.collisionBox.getBounds();
        Debugger.publishCollision("COLLISION", actor.collisionBox);

        final float EPSILON = 0.0001f;  // Needed to adjust right and bottom sides to not collide prematurely
                                        // in relation to left and top sides.
                                        // This is needed because the rendering occurs right to left, top to bottom

        // grid coordinates (columns)
        int left    = (int) Math.floor(box.x / TILE_SIZE);
        int right   = (int) Math.floor((box.x + box.width - EPSILON) / TILE_SIZE);
        int top     = (int) Math.floor(box.y / TILE_SIZE);
        int bottom  = (int) Math.floor((box.y + box.height - EPSILON) / TILE_SIZE);

        for (int row = top; row <= bottom; row++) {
            for (int col = left; col <= right; col++) {
                if (world.isSolid(row, col)) {
                    return true;
                }
            }
        }

        return false;
    }

}
