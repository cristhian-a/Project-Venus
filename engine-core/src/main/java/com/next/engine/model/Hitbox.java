package com.next.engine.model;

import com.next.engine.data.HitboxPool;
import com.next.engine.event.TriggerRule;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionCollector;

import java.util.HashSet;
import java.util.Set;

public class Hitbox extends Sensor {

    private Entity owner;
    private double remainingTime;
    private HitboxSpec specs;
    private final Set<Integer> alreadyHit = new HashSet<>();

    public Hitbox() {
        super();

//        switch (direction) {
//            case LEFT -> {
//                offsetX = offsetX * -1;
//                worldX = worldX - width;
//            }
//            case UP -> {
//                worldX -= height / 2 - 1;
//                worldY -= width;
//                float temp = height;
//                height = width;
//                width = temp;
//
//                temp = offsetX * -1;
//                offsetX = offsetY;
//                offsetY = temp;
//            }
//            case DOWN -> {
//                worldX -= height / 2 - 1;
//                float temp = height;
//                height = width;
//                width = temp;
//
//                temp = offsetX;
//                offsetX = offsetY * -1;
//                offsetY = temp;
//            }
//        }
    }

    public void init(Entity owner, float worldX, float worldY, HitboxSpec specs) {
        this.disposed = false;

        this.specs = specs;
        this.remainingTime = specs.durationSeconds();
        this.owner = owner;
        this.alreadyHit.clear();
        worldX += specs.offsetX();
        worldY += specs.offsetY();

        collisionBox = new CollisionBox(worldX, worldY, 0, 0, specs.width(), specs.height());
    }

    public void setRule(TriggerRule rule) {
        this.rule = rule;
    }

    @Override
    public void update(double delta) {
        remainingTime -= delta;
        if (remainingTime <= 0d) dispose();

        if (specs.followOwner()) followOwner();
    }

    @Override
    public void onCollision(Body other, CollisionCollector collector) {
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
        this.rule = null;
        this.collisionBox = null;

        HitboxPool.free(this);
    }

    private void followOwner() {
        collisionBox.update(owner.getWorldX() + specs.offsetX(), owner.getWorldY() + specs.offsetY());
    }
}
