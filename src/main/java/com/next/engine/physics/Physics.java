package com.next.engine.physics;

import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.engine.model.Actor;
import com.next.world.Scene;

import java.util.HashSet;
import java.util.Set;

public class Physics {

    private final Set<Long> processedPairs = new HashSet<>();

    private Scene scene;
    private SpatialGrid grid;
    private CollisionInspector inspector;

    public void setInspector(CollisionInspector inspector) {
        this.inspector = inspector;
        if (scene != null) inspector.inspecting(scene);
    }

    public void ruleOver(Scene scene) {
        this.scene = scene;

        int width = scene.world.getRules().columns() * scene.world.getTileSize();
        int height = scene.world.getRules().rows() * scene.world.getTileSize();
        grid = new SpatialGrid(width, height, scene.world.getTileSize());
        if (inspector != null) inspector.inspecting(scene);
    }

    public void apply(double delta, MotionQueue queue, Mailbox mailbox) {
        grid.clear();
        scene.forEachActor(grid::insert);

        for (int i = 0; i < queue.size(); i++) {
            moveX(delta, queue.actorId[i], queue.deltaX[i], mailbox);
            moveY(delta, queue.actorId[i], queue.deltaY[i], mailbox);
        }

        processedPairs.clear();
        queue.clear();
    }

    private void moveX(double delta, int actorId, float dx, Mailbox mailbox) {
        Actor actor = scene.getActors()[actorId];
        actor.moveX(dx);

        if (inspector.isCollidingWithTile(actor)) {
            clampX(actor, dx);
            return;
        }

        grid.queryNearby(actor, other -> {
            if (inspector.isColliding(actor, other)) {
                if (other == actor) return;

                long key = pairKey(actor, other);
                if (processedPairs.contains(key)) return; // pair already handled
                processedPairs.add(key);

                CollisionResult response = other.onCollision(new CollisionEvent(actor));
                if (response == null) return;

                if (response.type() == CollisionType.SOLID) {
                    float cx;
                    if (dx > 0) {
                        // Moving Right: Snap my Right to his Left
                        cx = other.getCollisionBox().getBounds().x - (actor.getCollisionBox().getBounds().width + actor.getCollisionBox().getOffsetX());
                    } else {
                        // Moving Left: Snap my Left to his Right
                        cx = (other.getCollisionBox().getBounds().x + other.getCollisionBox().getBounds().width) - actor.getCollisionBox().getOffsetX();
                    }

                    actor.setPosition((int) cx, actor.getWorldY());
                }

                if (response.eventFactory() != null) {
                    mailbox.eventSuppliers.add(response.eventFactory());
                }
            }
        });
    }

    private void moveY(double delta, int actorId, float dy, Mailbox mailbox) {
        Actor actor = scene.getActors()[actorId];
        actor.moveY(dy);

        if (inspector.isCollidingWithTile(actor)) {
            clampY(actor, dy);
            return;
        }

        grid.queryNearby(actor, other -> {
            if (inspector.isColliding(actor, other)) {
                if (other == actor) return;

                long key = pairKey(actor, other);
                if (processedPairs.contains(key)) return; // pair already handled
                processedPairs.add(key);

                CollisionResult response = other.onCollision(new CollisionEvent(actor));
                if (response == null) return;

                if (response.type() == CollisionType.SOLID) {
                    float cy;
                    if (dy > 0) {
                        // Moving Right: Snap my Right to his Left
                        cy = other.getCollisionBox().getBounds().y - (actor.getCollisionBox().getBounds().height + actor.getCollisionBox().getOffsetY());
                    } else {
                        // Moving Left: Snap my Left to his Right
                        cy = (other.getCollisionBox().getBounds().y + other.getCollisionBox().getBounds().height) - actor.getCollisionBox().getOffsetY();
                    }

                    actor.setPosition(actor.getWorldX(), (int) cy);
                }

                if (response.eventFactory() != null) {
                    mailbox.eventSuppliers.add(response.eventFactory());
                }
            }
        });
    }

    private void clampX(Actor actor, float dx) {
        var box = actor.getCollisionBox();

        float clampedX = calculateClamp(dx, box.getBounds().x, box.getBounds().width, box.getOffsetX());
        actor.setPosition((int) clampedX, actor.getWorldY());
    }

    private void clampY(Actor actor, float dy) {
        var box = actor.getCollisionBox();

        float clampedY = calculateClamp(dy, box.getBounds().y, box.getBounds().height, box.getOffsetY());
        actor.setPosition(actor.getWorldX(), (int) clampedY);
    }

    private float calculateClamp(float dMov, float boxAxisPosition, float boxAxisLength, float boxOffset) {
        float position;

        if (dMov > 0) {
            int tilePos = (int) ((boxAxisPosition + boxAxisLength) / scene.world.getTileSize());
            position = tilePos * scene.world.getTileSize() - (boxAxisLength + boxOffset);
        } else {
            int tilePos = (int) (boxAxisPosition / scene.world.getTileSize());
            position = (tilePos + 1) * scene.world.getTileSize() - boxOffset;
        }

        return position;
    }

    private long pairKey(Actor a, Actor b) {
        int ia = System.identityHashCode(a);
        int ib = System.identityHashCode(b);
        long min = Math.min(ia, ib);
        long max = Math.max(ia, ib);
        return (min << 32) | (max & 0xffffffffL);
    }
}
