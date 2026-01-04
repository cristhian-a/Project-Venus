package com.next.engine.physics;

import com.next.engine.event.EventCollector;
import com.next.engine.model.Sensor;
import com.next.world.Scene;

public class Physics implements SpatialGridHandler {

    private CollisionTable previous = new CollisionTable();
    private CollisionTable frameCollisions = new CollisionTable();

    private final CollisionInspector inspector = new CollisionInspector();

    private Scene scene;
    private SpatialGrid grid;

    public void ruleOver(Scene scene) {
        this.scene = scene;

        int width = scene.world.getRules().columns() * scene.world.getTileSize();
        int height = scene.world.getRules().rows() * scene.world.getTileSize();
        grid = new SpatialGrid(width, height, scene.world.getTileSize());
        inspector.inspecting(scene);
    }

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

    private void beginFrame() {
        var temp = previous;
        previous = frameCollisions;
        frameCollisions = temp;

        frameCollisions.clear();
    }

    /**
     * Apply the physics rules to the bodies inside the current ruled over {@link Scene}, then let the {@code collector}
     * collect all events produced during collision resolution.
     *
     * @param delta     delta time.
     * @param queue     a buffer with the requested motion deltas to be processed by the physics engine (see {@link MotionQueue}).
     * @param collector collects all events produced during collision resolution (see {@link EventCollector}).
     */
    public void apply(double delta, MotionQueue queue, EventCollector collector) {
        beginFrame();

        grid.clear();
        scene.forEachBody(grid::insert);

        for (int i = 0; i < queue.size(); i++) {
            integrateMotion(Axis.X, delta, queue.actorIds[i], queue.deltaX[i]);
            integrateMotion(Axis.Y, delta, queue.actorIds[i], queue.deltaY[i]);
        }

        collectStaticBodies(scene, grid);
        notify(collector);
    }

    private void integrateMotion(Axis axis, double deltaTime, int entityId, float motionDelta) {
        Body agent = (Body) scene.getEntity(entityId);
        if (agent == null) return;

        if (axis == Axis.X) {
            agent.moveX(motionDelta, deltaTime);
        } else if (axis == Axis.Y) {
            agent.moveY(motionDelta, deltaTime);
        }

        if (inspector.isCollidingWithTile(agent)) {
            clamp(axis, agent, motionDelta);
        }

        grid.queryBroadPhase(axis, agent, motionDelta, this);
    }

    protected void solveNarrowPhase(Axis axis, Body agent, Body other, float motionDelta) {
        if (inspector.isColliding(agent, other)) {
            if (other == agent) return;

            if (agent.getCollisionType() != CollisionType.NONE && other.getCollisionType() != CollisionType.NONE) {
                frameCollisions.add(agent, other);

                if (agent.getCollisionType() == CollisionType.SOLID && other.getCollisionType() == CollisionType.SOLID) {
                    updateAxisPosition(axis, agent, other, motionDelta);
                }
            }

        }
    }

    private void updateAxisPosition(Axis axis, Body agent, Body other, float motionDelta) {
        if (axis == Axis.X) {
            float rp = computeAxisSeparationPosition(
                    motionDelta,
                    agent.getCollisionBox().getBounds().width,
                    agent.getCollisionBox().getOffsetX(),
                    other.getCollisionBox().getBounds().x,
                    other.getCollisionBox().getBounds().width
            );
            agent.setPosition(rp, agent.getY());
        } else if (axis == Axis.Y) {
            float rp = computeAxisSeparationPosition(
                    motionDelta,
                    agent.getCollisionBox().getBounds().height,
                    agent.getCollisionBox().getOffsetY(),
                    other.getCollisionBox().getBounds().y,
                    other.getCollisionBox().getBounds().height
            );
            agent.setPosition(agent.getX(), rp);
        }
    }

    private void clamp(Axis axis, Body agent, float motionDelta) {
        var box = agent.getCollisionBox();

        if (axis == Axis.X) {
            float nx = computeTileSeparationPosition(motionDelta, box.getBounds().x, box.getBounds().width, box.getOffsetX());
            agent.setPosition(nx, agent.getY());
        } else if (axis == Axis.Y) {
            float ny = computeTileSeparationPosition(motionDelta, box.getBounds().y, box.getBounds().height, box.getOffsetY());
            agent.setPosition(agent.getX(), ny);
        }
    }

    private float computeTileSeparationPosition(
            float movementDelta,
            float agentMin, float agentSize, float agentOffset
    ) {
        if (movementDelta > 0) {
            int tilePos = (int) ((agentMin + agentSize) / scene.world.getTileSize());
            return tilePos * scene.world.getTileSize() - (agentSize + agentOffset);
        } else {
            int tilePos = (int) (agentMin / scene.world.getTileSize());
            return (tilePos + 1) * scene.world.getTileSize() - agentOffset;
        }
    }

    private float computeAxisSeparationPosition(
            float movementDelta,
            float agentSize, float agentOffset,
            float otherMin, float otherSize
    ) {
        if (movementDelta > 0) {
            return otherMin - (agentSize + agentOffset);
        } else {
            return (otherMin + otherSize) - agentOffset;
        }
    }

    private void collectStaticBodies(Scene scene, SpatialGrid grid) {
        for (int i = 0; i < scene.getSensorCount(); i++) {
            Sensor sensor = scene.getSensors()[i];
            processingSensorBox = sensor.getCollisionBox().getBounds();
            grid.queryNeighbors(sensor, this);
        }
    }

    // aux var only between collect and handleNeighbor
    // supposedly this helps by reducing the number of virtual calls in the JVM
    private AABB processingSensorBox;

    @Override
    public void handleNeighbor(Body self, Body neighbor) {
        if (neighbor.getCollisionBox().getBounds().intersects(processingSensorBox)) {
            frameCollisions.add(self, neighbor);
        }
    }

    /**
     * Evaluates the collision pairs produced in the current frame and notify entities
     * @param collector an {@link EventCollector} to be passed to the notified entities.
     */
    private void notify(EventCollector collector) {
        for (int i = 0; i < frameCollisions.size; i++) {
            Body a = frameCollisions.bodiesA[i];
            Body b = frameCollisions.bodiesB[i];

            boolean found = false;
            for (int j = 0; j < previous.size; j++) {
                if (frameCollisions.keys[i] == previous.keys[j]) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                a.onEnter(b, collector);
                b.onEnter(a, collector);
            }

            a.onCollision(b, collector);
            b.onCollision(a, collector);
        }

        for (int i = 0; i < previous.size; i++) {
            Body a = previous.bodiesA[i];
            Body b = previous.bodiesB[i];

            if (a == null || b == null) continue;

            boolean found = false;
            for (int j = 0; j < frameCollisions.size; j++) {
                if (previous.keys[i] == frameCollisions.keys[j]) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                a.onExit(b, collector);
                b.onExit(a, collector);
            }
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
        long[] keys = new long[64];     // when a stack overflow happens, we should reconsider our strategy
        Body[] bodiesA = new Body[64];  // to use hashes instead of linear scanning (when adding)
        Body[] bodiesB = new Body[64];
        int size = 0;

        void clear() {
            size = 0;
        }

        void add(Body a, Body b) {
            long pairKey = pairKey(a.getId(), b.getId());
            add(pairKey, a, b);
        }

        void add(long pairKey, Body a, Body b) {
            for (int i = 0; i < size; i++) {
                if (this.keys[i] == pairKey)
                    return;
            }

            bodiesA[size] = a;
            bodiesB[size] = b;
            this.keys[size] = pairKey;
            size++;
        }

        long pairKey(int aId, int bId) {
            long min = Math.min(aId, bId);
            long max = Math.max(aId, bId);
            return (min << 32) | (max & 0xffffffffL);
        }
    }
}
