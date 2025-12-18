package com.next.core.physics;

import com.next.model.Actor;
import com.next.world.Scene;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Physics {

    private final Set<Long> processedPairs = new HashSet<>();

    private Scene scene;
    private CollisionInspector inspector;

    public void setInspector(CollisionInspector inspector) {
        this.inspector = inspector;
        if (scene != null) inspector.inspecting(scene);
    }

    public void ruleOver(Scene scene) {
        this.scene = scene;
        if (inspector != null) inspector.inspecting(scene);
    }

    public void apply(double delta, List<Movement> intents) {
        for (Movement m : intents) {
            if (m != null) {
                moveX(m);
                moveY(m);
            }
        }

        scene.dismissDisposedActors();
        processedPairs.clear();
        intents.clear();
    }

    public void moveX(Movement movement) {
        Actor actor = movement.actor();
        actor.moveX(movement.dx());

        if (inspector.isCollidingWithTile(actor)) {
            clampX(movement);
            return;
        }

        for (Actor other: scene.actors) {
            if (other == actor) continue;
            if (inspector.isColliding(actor, other)) {
                long key = pairKey(actor, other);
                if (processedPairs.contains(key)) continue; // pair already handled
                processedPairs.add(key);

                CollisionResult response = other.onCollision(new CollisionEvent(actor));
                if (response == null) continue;

                if (response.type() == CollisionResult.Type.BLOCK) {
                    clampX(movement);
                }
            }
        }
    }

    public void moveY(Movement movement) {
        Actor actor = movement.actor();
        actor.moveY(movement.dy());

        if (inspector.isCollidingWithTile(actor)) {
            clampY(movement);
            return;
        }

        for (Actor other: scene.actors) {
            if (other == actor) continue;
            if (inspector.isColliding(actor, other)) {
                long key = pairKey(actor, other);
                if (processedPairs.contains(key)) continue; // pair already handled
                processedPairs.add(key);

                CollisionResult response = other.onCollision(new CollisionEvent(actor));
                if (response == null) continue;

                if (response.type() == CollisionResult.Type.BLOCK) {
                    clampY(movement);
                }
            }
        }
    }

    public void clampX(Movement movement) {
        Actor actor = movement.actor();
        var box = actor.getCollisionBox();

        float worldX;

        if (movement.dx() > 0) {
            int tileCol = (int) (box.getBounds().x + box.getBounds().width) / scene.world.getTileSize();
            worldX = tileCol * scene.world.getTileSize() - (box.getBounds().width + box.getOffsetX());
        } else {
            int tileCol = (int) box.getBounds().x / scene.world.getTileSize();
            worldX = (tileCol + 1) * scene.world.getTileSize() - box.getOffsetX();
        }

        actor.setPosition((int) worldX, actor.getWorldY());
    }

    public void clampY(Movement movement) {
        Actor actor = movement.actor();
        var box = actor.getCollisionBox();

        float worldY;

        if (movement.dy() > 0) {
            int tileRow = (int) (box.getBounds().y + box.getBounds().height) / scene.world.getTileSize();
            worldY = tileRow * scene.world.getTileSize() - (box.getBounds().height + box.getOffsetY());
        } else {
            int tileRow = (int) box.getBounds().y / scene.world.getTileSize();
            worldY = (tileRow + 1) * scene.world.getTileSize() - box.getOffsetY();
        }

        actor.setPosition(actor.getWorldX(), (int) worldY);
    }

    private long pairKey(Actor a, Actor b) {
        int ia = System.identityHashCode(a);
        int ib = System.identityHashCode(b);
        long min = Math.min(ia, ib);
        long max = Math.max(ia, ib);
        return (min << 32) | (max & 0xffffffffL);
    }
}
