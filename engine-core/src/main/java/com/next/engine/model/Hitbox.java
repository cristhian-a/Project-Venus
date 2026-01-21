package com.next.engine.model;

import com.next.engine.data.HitboxPool;
import com.next.engine.event.TriggerRule;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionBox;
import com.next.engine.event.EventCollector;

import java.util.HashSet;
import java.util.Set;

public class Hitbox extends Sensor {

    private Entity owner;
    private double remainingTime;
    private HitboxSpec specs;
    private final Set<Integer> alreadyHit = new HashSet<>();

    public Hitbox() {
        super();
    }

    public void init(Entity owner, float worldX, float worldY, HitboxSpec specs) {
        this.disposed = false;

        this.collisionMask = specs.collisionLayer();
        this.specs = specs;
        this.remainingTime = specs.durationSeconds();
        this.owner = owner;
        this.alreadyHit.clear();
        worldX += specs.offsetX();
        worldY += specs.offsetY();

        collisionBox = new CollisionBox(worldX, worldY, 0, 0, specs.width(), specs.height());
    }

    public final void setRule(TriggerRule rule) {
        this.onCollision = rule;
    }

    @Override
    public void update(double delta) {
        remainingTime -= delta;
        if (remainingTime <= 0d) dispose();

        if (specs.followOwner()) followOwner();
    }

    @Override
    public void onCollision(Body other, EventCollector collector) {
        if (other == owner)
            return;
        if (specs.oneHitPerTarget()) {
            if (alreadyHit.contains(other.getId())) return;
            alreadyHit.add(other.getId());
        }

        super.onCollision(other, collector);
    }

    @Override
    public void onDispose() {
        super.onDispose();

        this.owner = null;
        this.specs = null;
        this.remainingTime = 0d;
        this.alreadyHit.clear();
        this.onCollision = null;
        this.collisionBox = null;

        HitboxPool.free(this);
    }

    private void followOwner() {
        collisionBox.update(owner.getWorldX() + specs.offsetX(), owner.getWorldY() + specs.offsetY());
    }
}
