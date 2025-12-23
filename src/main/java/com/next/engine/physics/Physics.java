package com.next.engine.physics;

import com.next.engine.data.Mailbox;
import com.next.engine.model.Actor;
import com.next.world.Scene;

import java.util.HashSet;
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

    public void apply(double delta, Mailbox mailbox) {
        for (Movement m : mailbox.moveRequests) {
            if (m != null) {
                moveX(m, mailbox);
                moveY(m, mailbox);
            }
        }

        processedPairs.clear();
        mailbox.moveRequests.clear();
    }

    public void moveX(Movement movement, Mailbox mailbox) {
        Actor actor = movement.actor();
        actor.moveX(movement.dx());

        if (inspector.isCollidingWithTile(actor)) {
            clampX(movement);
            return;
        }

        scene.forEachActor((Actor other) -> {
            if (other == actor) return;
            if (inspector.isColliding(actor, other)) {
                long key = pairKey(actor, other);
                if (processedPairs.contains(key)) return; // pair already handled
                processedPairs.add(key);

                CollisionResult response = other.onCollision(new CollisionEvent(actor));
                if (response == null) return;

                if (response.type() == CollisionType.SOLID) {
                    clampX(movement);
                }

                if (response.eventFactory() != null) {
                    mailbox.eventSuppliers.add(response.eventFactory());
                }
            }
        });

//        for (int i = 0; i < scene.size(); i++) {
//            Actor other = scene.getActors()[i];
//            if (other == actor) continue;
//            if (inspector.isColliding(actor, other)) {
//                long key = pairKey(actor, other);
//                if (processedPairs.contains(key)) continue; // pair already handled
//                processedPairs.add(key);
//
//                CollisionResult response = other.onCollision(new CollisionEvent(actor));
//                if (response == null) continue;
//
//                if (response.type() == CollisionType.SOLID) {
//                    clampX(movement);
//                }
//
//                if (response.eventFactory() != null) {
//                    mailbox.eventSuppliers.add(response.eventFactory());
//                }
//            }
//        }
    }

    public void moveY(Movement movement, Mailbox mailbox) {
        Actor actor = movement.actor();
        actor.moveY(movement.dy());

        if (inspector.isCollidingWithTile(actor)) {
            clampY(movement);
            return;
        }

        for (int i = 0; i < scene.size(); i++) {
            Actor other = scene.getActors()[i];
            if (other == actor) continue;
            if (inspector.isColliding(actor, other)) {
                long key = pairKey(actor, other);
                if (processedPairs.contains(key)) continue; // pair already handled
                processedPairs.add(key);

                CollisionResult response = other.onCollision(new CollisionEvent(actor));
                if (response == null) continue;

                if (response.type() == CollisionType.SOLID) {
                    clampY(movement);
                }

                if (response.eventFactory() != null) {
                    mailbox.eventSuppliers.add(response.eventFactory());
                }
            }
        }
    }

    public void clampX(Movement movement) {
        Actor actor = movement.actor();
        var box = actor.getCollisionBox();

        float clampedX = calculateClamp(movement.dx(), box.getBounds().x, box.getBounds().width, box.getOffsetX());
        actor.setPosition((int) clampedX, actor.getWorldY());
    }

    public void clampY(Movement movement) {
        Actor actor = movement.actor();
        var box = actor.getCollisionBox();

        float clampedY = calculateClamp(movement.dy(), box.getBounds().y, box.getBounds().height, box.getOffsetY());
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
