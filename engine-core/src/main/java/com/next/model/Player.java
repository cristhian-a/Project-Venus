package com.next.model;

import com.next.engine.animation.Animation;
import com.next.engine.animation.AnimationState;
import com.next.engine.data.Mailbox;
import com.next.engine.model.AnimatedActor;
import com.next.engine.physics.*;
import com.next.engine.system.Debugger;
import com.next.event.DialogueEvent;
import com.next.system.Input;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player extends AnimatedActor {

    private enum Orientation {
        UP, DOWN, LEFT, RIGHT
    }

    @Getter private final List<Key> heldKeys = new ArrayList<>();
    @Setter private Input input;

    @Getter @Setter boolean talking;

    private float speed = 1;
    @Getter @Setter private int maxHp = 6;
    @Getter @Setter private int health = maxHp;
    private Orientation orientation = Orientation.DOWN;

    // Combat-related stuff
    private boolean attacking = false;
    private int attackingFrames = 0;
    private int invincibilityFrames = 0;
    private boolean invincible = false;

    public Player(float worldX, float worldY, Map<AnimationState, Animation> animations, CollisionBox collisionBox
    ) {
        this.worldX = worldX;
        this.worldY = worldY;

        layer = 1;
        collisionMask = 1;
        this.mass = 1f;

        this.collisionBox = collisionBox;
        this.collisionType = CollisionType.SOLID;

        setPosition(worldX, worldY);

        this.animations = animations;
        this.animationState = AnimationState.IDLE_DOWN;
    }

    @Override
    public void update(double delta, Mailbox mailbox) {
        float dx = 0;
        float dy = 0;
//        float speed = (float) (this.speed * delta);

        if (invincible) invincibilityFrames--;
        invincible = invincibilityFrames > 0;

        attacking = attackingFrames > 0;
        if (attacking) {
            attackingFrames--;
            switch (orientation) {
                case UP -> animationState = AnimationState.ATTACK_UP;
                case DOWN -> animationState = AnimationState.ATTACK_DOWN;
                case LEFT -> animationState = AnimationState.ATTACK_LEFT;
                case RIGHT -> animationState = AnimationState.ATTACK_RIGHT;
            }
        }

        if (input.isTyped(Input.Action.TALK) && !talking) {
            attacking = true;
            attackingFrames = 39;
        } else if (!talking & !attacking) {
            if (input.isDown(Input.Action.UP)) {
                dy -= speed;
                animationState = AnimationState.WALK_UP;
                orientation = Orientation.UP;
            } else if (input.isDown(Input.Action.DOWN)) {
                dy += speed;
                animationState = AnimationState.WALK_DOWN;
                orientation = Orientation.DOWN;
            } else if (input.isDown(Input.Action.LEFT)) {
                dx -= speed;
                animationState = AnimationState.WALK_LEFT;
                orientation = Orientation.LEFT;
            } else if (input.isDown(Input.Action.RIGHT)) {
                dx += speed;
                animationState = AnimationState.WALK_RIGHT;
                orientation = Orientation.RIGHT;
            }
        }

        if (dx != 0 || dy != 0)
            mailbox.motionQueue.submit(this.id, dx, dy, 0f);
        else {
            if (!attacking) {
                switch (orientation) {
                    case UP -> animationState = AnimationState.IDLE_UP;
                    case DOWN -> animationState = AnimationState.IDLE_DOWN;
                    case LEFT -> animationState = AnimationState.IDLE_LEFT;
                    case RIGHT -> animationState = AnimationState.IDLE_RIGHT;
                }
            }
        }

        animate();

        Debugger.publish("PLAYER", new Debugger.DebugText("X: " + worldX + ", Y: " + worldY), 10, 90, Debugger.TYPE.INFO);
        Debugger.publish("HITBOX", new Debugger.DebugText("X: " + collisionBox.getBounds().x + ", Y: " + collisionBox.getBounds().y + ", Width: " + collisionBox.getBounds().width + ", Height: " + collisionBox.getBounds().height), 10, 120, Debugger.TYPE.INFO);
    }

    @Override
    public void animate() {
        animator.set(animations.get(animationState));
        spriteId = animator.update();
    }

    @Override
    public void onCollision(Body other, CollisionCollector collector) {
        if (other instanceof NpcDummy dummy) {
            if (input.isTyped(Input.Action.TALK)) {
                collector.post(() -> new DialogueEvent(this, dummy));
            }
        }
    }

    public void boostSpeed(float boost) {
        speed += boost;
    }

    public void takeDamage(int damage) {
        if (invincible) return;

        damage = Math.clamp(damage, 0, health);
        health -= damage;
        invincible = true;
        invincibilityFrames = 60;
    }
}
