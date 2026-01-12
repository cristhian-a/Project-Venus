package com.next.game.model;

import com.next.engine.animation.Animation;
import com.next.engine.animation.AnimationState;
import com.next.engine.data.Mailbox;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionBox;
import com.next.engine.event.EventCollector;
import com.next.engine.physics.CollisionType;
import com.next.game.rules.Layers;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class NpcDummy extends Npc {

    @Getter @Setter private boolean behave = true;
    private int movementFrames = 50;
    private int direction = 0;

    public NpcDummy(float worldX, float worldY, Map<AnimationState, Animation> animations) {
        this.spriteId = 33;
        this.animations.putAll(animations);
        this.collisionType = CollisionType.SOLID;
        this.animationState = AnimationState.IDLE;
        this.collisionBox = new CollisionBox(-5, -4, 10, 12);

        this.layer = Layers.NPC;
        this.collisionMask = Layers.PLAYER | Layers.WALL;

        setPosition(worldX, worldY);
    }

    @Override
    public void animate() {
        super.animate();
    }

    @Override
    public void update(double delta, Mailbox mailbox) {
        if (behave) behave(mailbox);

        animate();
    }

    @Override
    public void onCollision(Body other, EventCollector collector) {
    }

    private void behave(Mailbox mailbox) {
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
        if (direction == 0) {
            animationState = AnimationState.IDLE;
        } else if (direction == 1) {
            dx += speed;
            animationState = AnimationState.WALK_RIGHT;
        } else if (direction == 2) {
            dx -= speed;
            animationState = AnimationState.WALK_LEFT;
        } else if (direction == 3) {
            dy -= speed;
            animationState = AnimationState.WALK_UP;
        } else if (direction == 4) {
            dy += speed;
            animationState = AnimationState.WALK_DOWN;
        }

        mailbox.motionQueue.submit(this.id, dx, dy, 0f);
    }

}
