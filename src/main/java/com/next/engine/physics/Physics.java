package com.next.engine.physics;

import com.next.engine.data.Mailbox;
import com.next.world.Scene;

public class Physics {

    private final CollisionTable collisionTable = new CollisionTable();

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

    /**
     * This is totally broken right now, but when working, it should let actors push each other around.
     *
     * @param delta   delta time.
     * @param queue   a buffer with the requested motion deltas to be processed by the physics engine.
     * @param mailbox a frame context bus.
     */
//    public void applyNewtonPhysics(double delta, MotionQueue queue, Mailbox mailbox) {
//        float dt = (float) delta;
//
//        // set velocities from motion requests and tentative positions
//        for (int i = 0; i < queue.size(); i++) {
//            int id = queue.actorIds[i];
//            Actor actor = scene.getActors()[id];
//
//            float desiredDx = queue.deltaX[i];
//            float desiredDy = queue.deltaY[i];
//
//            actor.setVelocity(desiredDx / dt, desiredDy / dt);
//            actor.setPosition(actor.getWorldX() + desiredDx, actor.getWorldY() + desiredDy);
//        }
//
//        grid.clear();
//        scene.forEachActor(grid::insert);
//
//        // collect contacts (one contact per pair; choose axis of least penetration and set normal)
//        Map<Long, Contact> contacts = new HashMap<>();
//        for (int i = 0; i < scene.size(); i++) {
//            Actor actor = scene.getActors()[i];
//            grid.forEachNearby(actor, other -> {
//                if (other == actor) return;
//                collisionTable.add(actor, other);
//
//                if (!inspector.isColliding(actor, other)) return;
//
//                // compute overlaps
//                float xOverlap = computeAxisOverlap(actor, other, Axis.X);
//                float yOverlap = computeAxisOverlap(actor, other, Axis.Y);
//                if (xOverlap <= 0f && yOverlap <= 0f) return;
//
//                // choosing axis with the least penetration to compute normal and penetration
//                boolean useX = (xOverlap > 0f && yOverlap > 0f) ? xOverlap < yOverlap : xOverlap > 0f;
//                float penetration = useX ? xOverlap : yOverlap;
//                float nx = 0;
//                float ny = 0;
//
//                if (useX) {
//                    // normal points from a to b along X
//                    float aCenter = actor.getCollisionBox().getBounds().x + actor.getCollisionBox().getBounds().width * 0.5f;
//                    float oCenter = other.getCollisionBox().getBounds().x + other.getCollisionBox().getBounds().width * 0.5f;
//                    nx = aCenter < oCenter ? -1f : 1f;  // normal direction matters with impulse sign later
//                } else {
//                    float aCenter = actor.getCollisionBox().getBounds().y + actor.getCollisionBox().getBounds().height * 0.5f;
//                    float oCenter = other.getCollisionBox().getBounds().y + other.getCollisionBox().getBounds().height * 0.5f;
//                    ny = aCenter < oCenter ? -1f : 1f;
//                }
//
//                CollisionResult response = other.onCollision(actor);
//                contacts.put(0L, new Contact(actor.getId(), other.getId(), penetration, nx, ny, response));
//            });
//        }
//
//        // Impulse solving (several iteractions) - remove relative velocity along normal
//        final int IMP_ITER = 6;
//        final float RESTITUTION = 0f;
//        for (int it = 0; it < IMP_ITER; it++) {
//            for (Contact contact : contacts.values()) {
//                if (contact.result == null) continue;
//
//                Actor A = scene.getActors()[contact.aIdx];
//                Actor B = scene.getActors()[contact.bIdx];
//
//                float invA = A.invMass();
//                float invB = B.invMass();
//
//                float rvx = B.vx - A.vx;
//                float rvy = B.vy - A.vy;
//                float velAlongNormal = rvx * contact.nx + rvy * contact.ny;
//                if (velAlongNormal > 0f) continue;  // separating, don't apply impulse
//
//                float j = -(1f + RESTITUTION) * velAlongNormal;
//                float denom = invA + invB;
//                if (denom <= 0f) continue;
//                j /= denom;
//
//                float impulseX = j * contact.nx;
//                float impulseY = j * contact.ny;
//
//                A.vx -= invA * impulseX;
//                A.vy -= invA * impulseY;
//                B.vx += invB * impulseX;
//                B.vy += invB * impulseY;
//            }
//        }
//
//        // positional correction (Baumgarte) to remove residual penetration
//        final float SLOP = 0.01f;
//        final float CORR = 0.2f;
//        for (Contact contact : contacts.values()) {
//            if (contact.result == null) continue;
//
//            Actor A = scene.getActors()[contact.aIdx];
//            Actor B = scene.getActors()[contact.bIdx];
//
//            float invA = A.invMass();
//            float invB = B.invMass();
//            float denom = invA + invB;
//            if (denom <= 0f) continue;
//
//            float pen = Math.max(contact.penetration - SLOP, 0f);
//            if (pen <= 0f) continue;
//
//            float correction = (pen / denom) * CORR;
//
//            float ax = correction * invA * contact.nx;
//            float ay = correction * invA * contact.ny;
//            float bx = correction * invB * contact.nx;
//            float by = correction * invB * contact.ny;
//
//            A.setPosition(A.getWorldX() - ax, A.getWorldY() - ay);
//            B.setPosition(B.getWorldX() + bx, B.getWorldY() + by);
//        }
//
//        for (Contact contact : contacts.values()) {
//            if (contact.result != null && contact.result.eventFactory() != null) {
//                mailbox.eventSuppliers.add(contact.result.eventFactory());
//            }
//        }
//
//        collisionTable.clear();
//        queue.clear();
//    }
//
//    private float computeAxisOverlap(Actor actor, Actor other, Axis axis) {
//        AABB A = actor.getCollisionBox().getBounds();
//        AABB B = other.getCollisionBox().getBounds();
//
//        if (axis == Axis.X) {
//            float aMin = A.x;
//            float aMax = A.x + A.width;
//            float bMin = B.x;
//            float bMax = B.x + B.width;
//            return Math.min(aMax, bMax) - Math.max(aMin, bMin);
//        } else if (axis == Axis.Y) {
//            float aMin = A.y;
//            float aMax = A.y + A.height;
//            float bMin = B.y;
//            float bMax = B.y + B.height;
//            return Math.min(aMax, bMax) - Math.max(aMin, bMin);
//        } else {
//            return 0f;
//        }
//    }

    public void apply(double delta, MotionQueue queue, Mailbox mailbox) {
        grid.clear();
        scene.forEachBody(grid::insert);

        for (int i = 0; i < queue.size(); i++) {
            moveX(delta, queue.actorIds[i], queue.deltaX[i]);
            moveY(delta, queue.actorIds[i], queue.deltaY[i]);
        }

        for (int i = 0; i < collisionTable.count; i++) {
            Body a = collisionTable.actorsA[i];
            Body b = collisionTable.actorsB[i];

            CollisionResult responseA = a.onCollision(b);
            CollisionResult responseB = b.onCollision(a);

            if (responseA != null && responseA.eventFactory() != null)
                mailbox.eventSuppliers.add(responseA.eventFactory());

            if (responseB != null && responseB.eventFactory() != null)
                mailbox.eventSuppliers.add(responseB.eventFactory());
        }

        collisionTable.clear();
        queue.clear();
    }

    private void moveX(double delta, int actorId, float dx) {
        Body actor = scene.findBodyById(actorId);
        actor.moveX(dx, delta);

        if (inspector.isCollidingWithTile(actor)) {
            clampX(actor, dx);
            return;
        }

        grid.forEachNearby(actor, other -> {
            resolveMoveX(actor, other, dx);
        });
    }

    protected void resolveMoveX(Body actor, Body other, float dx) {
        if (inspector.isColliding(actor, other)) {
            if (other == actor) return;

            if (actor.getCollisionType() != CollisionType.NONE && other.getCollisionType() != CollisionType.NONE) {
                collisionTable.add(actor, other);

                if (actor.getCollisionType() == CollisionType.SOLID && other.getCollisionType() == CollisionType.SOLID) {
                    float rx = computeAxisSeparationPosition(
                            dx,
                            actor.getCollisionBox().getBounds().width,
                            actor.getCollisionBox().getOffsetX(),
                            other.getCollisionBox().getBounds().x,
                            other.getCollisionBox().getBounds().width
                    );

                    actor.setPosition(rx, actor.getY());
                }
            }

        }
    }

    private void moveY(double delta, int actorId, float dy) {
        Body actor = scene.findBodyById(actorId);
        actor.moveY(dy, delta);

        if (inspector.isCollidingWithTile(actor)) {
            clampY(actor, dy);
            return;
        }

        grid.forEachNearby(actor, other -> {
            resolveMoveY(actor, other, dy);
        });
    }

    protected void resolveMoveY(Body actor, Body other, float dy) {
        if (inspector.isColliding(actor, other)) {
            if (other == actor) return;

            if (actor.getCollisionType() != CollisionType.NONE && other.getCollisionType() != CollisionType.NONE) {
                collisionTable.add(actor, other);

                if (actor.getCollisionType() == CollisionType.SOLID && other.getCollisionType() == CollisionType.SOLID) {
                    float ry = computeAxisSeparationPosition(
                            dy,
                            actor.getCollisionBox().getBounds().height,
                            actor.getCollisionBox().getOffsetY(),
                            other.getCollisionBox().getBounds().y,
                            other.getCollisionBox().getBounds().height
                    );

                    actor.setPosition(actor.getX(), ry);
                }
            }
        }
    }

    private void clampX(Body actor, float dx) {
        var box = actor.getCollisionBox();

        float clampedX = computeTileSeparationPosition(dx, box.getBounds().x, box.getBounds().width, box.getOffsetX());
        actor.setPosition(clampedX, actor.getY());
    }

    private void clampY(Body actor, float dy) {
        var box = actor.getCollisionBox();

        float clampedY = computeTileSeparationPosition(dy, box.getBounds().y, box.getBounds().height, box.getOffsetY());
        actor.setPosition(actor.getX(), clampedY);
    }

    private float computeTileSeparationPosition(
            float movementDelta,
            float actorMin, float actorSize, float actorOffset
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
            return (otherMin + otherSize) - actorOffset;
        }
    }

    // contact describing pair + penetration + normal (axis aligned)
    private static final class Contact {
        final int aIdx, bIdx;
        float penetration;    // positive depth
        float nx, ny;         // normal pointing from A -> B (unit along axis, e.g. (1,0) or (0,1))
        CollisionResult result;

        Contact(int aIdx, int bIdx, float penetration, float nx, float ny, CollisionResult res) {
            this.aIdx = aIdx;
            this.bIdx = bIdx;
            this.penetration = penetration;
            this.nx = nx;
            this.ny = ny;
            this.result = res;
        }
    }

    private static final class CollisionTable {
        long[] keys     = new long[64];     // when a stack overflow happens, we should reconsider our strategy
        Body[] actorsA  = new Body[64];     // to use hashes instead of linear scanning (when adding)
        Body[] actorsB  = new Body[64];
        int count = 0;

        void clear() {
            count = 0;
        }

        void add(Body a, Body b) {
            long pairKey = pairKey(a.getId(), b.getId());
            add(pairKey, a, b);
        }

        void add(long pairKey, Body a, Body b) {
            for (int i = 0; i < count; i++) {
                if (this.keys[i] == pairKey)
                    return;
            }

            actorsA[count] = a;
            actorsB[count] = b;
            this.keys[count] = pairKey;
            count++;
        }

        long pairKey(int aId, int bId) {
            long min = Math.min(aId, bId);
            long max = Math.max(aId, bId);
            return (min << 32) | (max & 0xffffffffL);
        }
    }
}
