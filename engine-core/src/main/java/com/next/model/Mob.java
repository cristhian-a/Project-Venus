package com.next.model;

import com.next.engine.animation.Animation;
import com.next.engine.animation.AnimationState;
import com.next.engine.data.Mailbox;
import com.next.engine.model.AnimatedActor;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionCollector;
import com.next.engine.physics.CollisionType;
import com.next.event.DamageEvent;

import java.util.Map;

public class Mob extends AnimatedActor implements Combatant {

    private int maxHealth = 4;
    private int health = maxHealth;

    public Mob(Map<AnimationState, Animation> animations, float x, float y, float width, float height, float offsetX, float offsetY) {
        this.animations = animations;
        this.worldX = x;
        this.worldY = y;
        this.animationState = AnimationState.IDLE;
        this.collisionType = CollisionType.SOLID;
        this.collisionBox = new CollisionBox(x, y, offsetX, offsetY, width, height);
        this.layer = 1;
    }

    @Override
    public void update(double delta, Mailbox mailbox) {
        animate();
    }

    @Override
    public void onCollision(Body other, CollisionCollector collector) {
        if (other instanceof Player p) {
            collector.post(() -> new DamageEvent(p, 1));
        }
    }

    @Override
    public void takeDamage(int damage) {
        damage = Math.clamp(damage, 0, health);
        health -= damage;

        if (health == 0) {
            this.dispose();
        }
    }
}
