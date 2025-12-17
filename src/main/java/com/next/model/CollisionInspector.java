package com.next.model;

import com.next.world.World;

public class CollisionInspector {

    private World world;
    private int tileSize;

    public CollisionInspector(World world) {
        this.world = world;
        tileSize = world.getTileSize();
    }

    public int getTileSize() {
        return world.getTileSize();
    }

    public void setWorld(World world) {
        this.world = world;
        tileSize = world.getTileSize();
    }

    public boolean isCollidingWithTile(Actor actor) {
        AABB box = actor.collisionBox.getBounds();

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
                if (world.isSolid(row, col)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isColliding(Actor actor, Actor other) {
        boolean is = actor.collisionBox.intersects(other.collisionBox);
        if (is) IO.println("COLLISION");
        return is;
    }

}
