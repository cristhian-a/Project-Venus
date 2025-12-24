package com.next.engine.physics;

import com.next.engine.data.Mailbox;
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
            moveX(delta, queue.actorIds[i], queue.deltaX[i], mailbox);
            moveY(delta, queue.actorIds[i], queue.deltaY[i], mailbox);
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
                    float rx = computeAxisSeparationPosition(
                            dx,
                            actor.getCollisionBox().getBounds().width,
                            actor.getCollisionBox().getOffsetX(),
                            other.getCollisionBox().getBounds().x,
                            other.getCollisionBox().getBounds().width
                    );

                    actor.setPosition((int) rx, actor.getWorldY());
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
                    float ry = computeAxisSeparationPosition(
                            dy,
                            actor.getCollisionBox().getBounds().height,
                            actor.getCollisionBox().getOffsetY(),
                            other.getCollisionBox().getBounds().y,
                            other.getCollisionBox().getBounds().height
                    );

                    actor.setPosition(actor.getWorldX(), (int) ry);
                }

                if (response.eventFactory() != null) {
                    mailbox.eventSuppliers.add(response.eventFactory());
                }
            }
        });
    }

    private void clampX(Actor actor, float dx) {
        var box = actor.getCollisionBox();

        float clampedX = computeTileSeparationPosition(dx, box.getBounds().x, box.getBounds().width, box.getOffsetX());
        actor.setPosition((int) clampedX, actor.getWorldY());
    }

    private void clampY(Actor actor, float dy) {
        var box = actor.getCollisionBox();

        float clampedY = computeTileSeparationPosition(dy, box.getBounds().y, box.getBounds().height, box.getOffsetY());
        actor.setPosition(actor.getWorldX(), (int) clampedY);
    }

    private float computeTileSeparationPosition(
            float movementDelta, float actorMin, float actorSize, float actorOffset
    ) {
        if (movementDelta > 0) {
            int tilePos = (int) ((actorMin + actorSize) / scene.world.getTileSize());
            return tilePos * scene.world.getTileSize() - (actorSize + actorOffset);
        } else {
            int tilePos = (int) (actorMin / scene.world.getTileSize());
            return (tilePos + 1) * scene.world.getTileSize() - actorOffset;
        }
    }

    private float computeAxisSeparationPosition(
            float movementDelta,
            float actorSize, float actorOffset,
            float otherMin, float otherSize
    ) {
        if (movementDelta > 0) {
            return otherMin - (actorSize + actorOffset);
        } else {
            return  (otherMin + otherSize) - actorOffset;
        }
    }

    private long pairKey(Actor a, Actor b) {
        int ia = System.identityHashCode(a);
        int ib = System.identityHashCode(b);
        long min = Math.min(ia, ib);
        long max = Math.max(ia, ib);
        return (min << 32) | (max & 0xffffffffL);
    }
}
