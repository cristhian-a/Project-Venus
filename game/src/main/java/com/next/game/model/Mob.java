package com.next.game.model;

import com.next.engine.Global;
import com.next.engine.animation.Animation;
import com.next.engine.animation.AnimationState;
import com.next.engine.data.Mailbox;
import com.next.engine.model.AnimatedActor;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionBox;
import com.next.engine.event.EventCollector;
import com.next.engine.physics.CollisionType;
import com.next.game.event.FallDamageEvent;
import com.next.game.rules.data.Attributes;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class Mob extends AnimatedActor implements Combatant {

    @Getter private final int maxHealth = 3;
    @Getter @Setter private int health = maxHealth;

    @Getter private final Attributes attributes;

    private double deathTimer = 0;
    private double stunTimer = 0;
    private int movementFrames = 50;
    private int direction = 0;

    public Mob(Map<AnimationState, Animation> animations, float x, float y, float width, float height, float offsetX, float offsetY) {
        this.animations = animations;
        this.worldX = x;
        this.worldY = y;
        this.animationState = AnimationState.IDLE;
        this.collisionType = CollisionType.SOLID;
        this.collisionBox = new CollisionBox(x, y, offsetX, offsetY, width, height);
        this.layer = 1;

        this.attributes = new Attributes();
        attributes.strength = 1;
        attributes.resistance = 0;
        attributes.level = 1;
    }

    @Override
    public void update(double delta, Mailbox mailbox) {
        if (deathTimer > 0d) {
            deathTimer -= delta;
            if (deathTimer <= 0d) {
                dispose();
                return;
            }
        }

        if (stunTimer > 0d) {
            stunTimer -= delta;
            stunTimer = Math.max(0d, stunTimer);
            return;
        }

        behave(mailbox);
        animate();
    }

    @Override
    public void onCollision(Body other, EventCollector collector) {
        if (other instanceof Player p) {
            collector.post(() -> new FallDamageEvent(p, 1));
        }
    }

    @Override
    public int getAttack() {
        return attributes.strength;
    }

    @Override
    public int getDefense() {
        return attributes.resistance;
    }

    @Override
    public void takeDamage(int damage) {
        if (deathTimer > 0) return;

        damage = Math.clamp(damage, 0, health);
        health -= damage;

        if (health == 0) {
            die();
        } else {
            stunTimer = Global.fixedDelta * 30;
        }
    }

    @Override
    public boolean isDead() {
        return health == 0;
    }

    private void die() {
        animationState = AnimationState.DEAD;
        deathTimer = Global.fixedDelta * 45;
    }

    private void behave(Mailbox mailbox) {
        if (health <= 0) return;

        long n = System.nanoTime();
        long rng = n % 35;

        float dx = 0, dy = 0;

        if (movementFrames-- <= 0) {
            movementFrames = 50;

            if (rng < 7) {
                direction = 1;
            } else if (rng < 14) {
                direction = 2;
            } else if (rng < 21) {
                direction = 3;
            } else if (rng < 28){
                direction = 4;
            } else {
                direction = 0;
            }
        }

        float speed = 0.5f;
        if (direction == 1) {
            dx += speed;
        } else if (direction == 2) {
            dx -= speed;
        } else if (direction == 3) {
            dy -= speed;
        } else if (direction == 4) {
            dy += speed;
        }

        mailbox.motionQueue.submit(this.id, dx, dy, 0f);
    }
}
