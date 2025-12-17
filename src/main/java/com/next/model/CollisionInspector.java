package com.next.model;

import com.next.world.Scene;

import java.util.HashMap;
import java.util.Map;

public class CollisionInspector {

    private Scene scene;
    private int tileSize;

    private Map<Actor, Actor> lastCollisions = new HashMap<>();

    public void inspect(Scene scene) {
        this.scene = scene;
        tileSize = scene.world.getTileSize();
    }

    public int getTileSize() {
        return scene.world.getTileSize();
    }

    public boolean isColliding(Actor actor) {
        return isCollidingWithTile(actor) || isCollidingWithActors(actor);
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
                if (scene.world.isSolid(row, col)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isCollidingWithActors(Actor actor) {
        for (Actor other : scene.actors) {
            if (other != null && other != actor && isColliding(actor, other)) return true;
        }
        return false;
    }

    public boolean isColliding(Actor actor, Actor other) {
        boolean is = actor.collisionBox.intersects(other.collisionBox);
        if (is) lastCollisions.put(actor, other);
        return is;
    }

    public Actor getLastCollisionWithActor(Actor actor) {
        return lastCollisions.get(actor);
    }

}
