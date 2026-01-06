package com.next.game.model;

import com.next.engine.Global;
import com.next.engine.animation.Animation;
import com.next.engine.animation.AnimationState;
import com.next.engine.data.Mailbox;
import com.next.engine.event.EventCollector;
import com.next.engine.event.TriggerRules;
import com.next.engine.model.AnimatedActor;
import com.next.engine.model.HitboxSpec;
import com.next.engine.physics.*;
import com.next.engine.scene.Direction;
import com.next.engine.system.DebugChannel;
import com.next.engine.system.Debugger;
import com.next.game.event.AttackEvent;
import com.next.game.model.factory.HitboxFactory;
import com.next.game.rules.data.ActiveGear;
import com.next.game.rules.data.Attributes;
import com.next.engine.system.Input;
import com.next.game.util.Inputs;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player extends AnimatedActor implements Combatant {

    @Getter private final List<Key> heldKeys = new ArrayList<>();
    @Setter private Input input;
    private final HitboxFactory hitboxFactory;

    @Getter @Setter boolean talking;

    private float speed = 1;
    @Getter @Setter private int maxHealth = 6;
    @Getter @Setter private int health = maxHealth;
    private Direction direction = Direction.DOWN;

    private float dx;
    private float dy;

    // Combat-related stuff
    private boolean attacking = false;
    private int attackingFrames = 0;
    private int invincibilityFrames = 0;
    private boolean invincible = false;
    @Getter private final ActiveGear activeGear;
    @Getter private final Attributes attributes;

    public Player(float worldX, float worldY, Map<AnimationState, Animation> animations, CollisionBox collisionBox,
                  HitboxFactory hitboxFactory
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
        this.hitboxFactory = hitboxFactory;

        // Gear and stuff
        this.activeGear = new ActiveGear();
        activeGear.weapon = new EquipSword();
        activeGear.shield = new EquipShieldWood();

        this.attributes = new Attributes();
        attributes.strength = 1;
        attributes.resistance = 1;
        attributes.coin = 0;
        attributes.level = 1;
        attributes.xp = 0;
    }

    @Override
    public void update(double delta, Mailbox mailbox) {

        if (invincible) invincibilityFrames--;
        invincible = invincibilityFrames > 0;

        attacking = attackingFrames > 0;
        handleAttack();
        if (attacking) {
            attackingFrames--;
            switch (direction) {
                case UP -> animationState = AnimationState.ATTACK_UP;
                case DOWN -> animationState = AnimationState.ATTACK_DOWN;
                case LEFT -> animationState = AnimationState.ATTACK_LEFT;
                case RIGHT -> animationState = AnimationState.ATTACK_RIGHT;
            }
        }

        if (input.isTyped(Inputs.TALK) && !talking && !attacking) {
            attacking = true;
            attackingFrames = 39;
        } else if (!talking & !attacking) {
            if (input.isDown(Inputs.UP)) {
                dy -= speed;
                animationState = AnimationState.WALK_UP;
                direction = Direction.UP;
            } else if (input.isDown(Inputs.DOWN)) {
                dy += speed;
                animationState = AnimationState.WALK_DOWN;
                direction = Direction.DOWN;
            } else if (input.isDown(Inputs.LEFT)) {
                dx -= speed;
                animationState = AnimationState.WALK_LEFT;
                direction = Direction.LEFT;
            } else if (input.isDown(Inputs.RIGHT)) {
                dx += speed;
                animationState = AnimationState.WALK_RIGHT;
                direction = Direction.RIGHT;
            }
        }

        if (dx != 0 || dy != 0)
            mailbox.motionQueue.submit(this.id, dx, dy, 0f);
        else {
            if (!attacking) {
                switch (direction) {
                    case UP -> animationState = AnimationState.IDLE_UP;
                    case DOWN -> animationState = AnimationState.IDLE_DOWN;
                    case LEFT -> animationState = AnimationState.IDLE_LEFT;
                    case RIGHT -> animationState = AnimationState.IDLE_RIGHT;
                }
            }
        }

        animate();
        dx = 0;
        dy = 0;

        Debugger.publish("PLAYER", new Debugger.DebugText("X: " + worldX + ", Y: " + worldY), 10, 90, DebugChannel.INFO);
        Debugger.publish("HITBOX", new Debugger.DebugText("X: " + collisionBox.getBounds().x + ", Y: " + collisionBox.getBounds().y + ", Width: " + collisionBox.getBounds().width + ", Height: " + collisionBox.getBounds().height), 10, 120, DebugChannel.INFO);
    }

    @Override
    public void animate() {
        animator.set(animations.get(animationState));
        spriteId = animator.update();
    }

    @Override
    public void onCollision(Body other, EventCollector collector) {
//        if (other instanceof NpcDummy dummy) {
//            if (input.isTyped(Input.Action.TALK)) {
//                collector.post(() -> new DialogueEvent(this, dummy));
//            }
//        }
    }

    @Override
    public void takeDamage(int damage) {
        if (invincible) return;

        damage = Math.clamp(damage, 0, health);
        health -= damage;
        invincible = true;
        invincibilityFrames = 60;
    }

    @Override
    public boolean isDead() {
        return false;
    }

    public void boostSpeed(float boost) {
        speed += boost;
    }

    @Override
    public int getAttack() {
        return attributes.strength * activeGear.weapon.getMight();
    }

    @Override
    public int getDefense() {
        return attributes.resistance * activeGear.shield.getResistance();
    }

    public void handleAttack() {
        if (attacking && attackingFrames > 29) {
            if (direction == Direction.UP) dy -= speed;
            if (direction == Direction.DOWN) dy += speed;
            if (direction == Direction.LEFT) dx -= speed;
            if (direction == Direction.RIGHT) dx += speed;
        }

        if (attackingFrames == 30) {
            var spec = getWeaponSpecs();
            var rule = TriggerRules
                    .when((s, other) -> other instanceof Combatant)
                    .then((s, other) -> new AttackEvent(this, (Combatant) other, spec));
            hitboxFactory.spawnHitbox(this, spec, rule);
        }
    }

    // weapon specs
    private HitboxSpec downSpec, upSpec, leftSpec, rightSpec;

    public HitboxSpec getWeaponSpecs() {
        double duration = Global.fixedDelta * 30d;
        float knockback = 0.5f * speed;

        switch (direction) {
            case DOWN -> {
                if (downSpec == null) {
                    downSpec = new HitboxSpec(-2, 7, 5, 14,
                            duration, 1, 0, knockback,
                            collisionMask, true, true);
                }
                return downSpec;
            }
            case UP -> {
                if (upSpec == null) {
                    upSpec = new HitboxSpec(-2, -18, 5, 14,
                            duration, 1, 0, -knockback,
                            collisionMask, true, true);
                }
                return upSpec;
            }
            case LEFT -> {
                if (leftSpec == null) {
                    leftSpec = new HitboxSpec(-20, 0, 14, 5,
                            duration, 1, -knockback, 0,
                            collisionMask, true, true);
                }
                return leftSpec;
            }
            case RIGHT -> {
                if (rightSpec == null) {
                    rightSpec = new HitboxSpec(6, 0, 14, 5,
                            duration, 1, knockback, 0,
                            collisionMask, true, true);
                }
                return rightSpec;
            }
        }

        throw new RuntimeException("Invalid direction");
    }
}
