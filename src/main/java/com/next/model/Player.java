package com.next.model;

import com.next.core.Animation;
import com.next.core.AnimationState;
import com.next.core.data.Mailbox;
import com.next.core.physics.CollisionBox;
import com.next.core.physics.CollisionType;
import com.next.core.physics.Movement;
import com.next.system.Debugger;
import com.next.system.Input;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Player extends AnimatedActor {

    @Getter private final List<Key> heldKeys = new ArrayList<>();

    private float speed = 1f;

    public Player(int spriteId, float worldX, float worldY,
                  Animation upAnim, Animation downAnim, Animation leftAnim, Animation rightAnim
    ) {
        this.worldX = worldX;
        this.worldY = worldY;

        layer = 1;
        collisionMask = 1;

        collisionBox = new CollisionBox(3, 6, 10, 10);
        this.collisionType = CollisionType.SOLID;

        setPosition(worldX, worldY);

        animationState = AnimationState.IDLE;
        Animation idle = new Animation(new int[] { spriteId}, 0, false);

        animations.put(AnimationState.IDLE, idle);
        animations.put(AnimationState.WALK_UP, upAnim);
        animations.put(AnimationState.WALK_DOWN, downAnim);
        animations.put(AnimationState.WALK_LEFT, leftAnim);
        animations.put(AnimationState.WALK_RIGHT, rightAnim);
    }

    public void update(double delta, Input input, Mailbox mailbox) {
        float dx = 0;
        float dy = 0;

        animationState = AnimationState.IDLE;

        if (input.isDown(Input.Action.UP)) {
            dy -= speed;
            animationState = AnimationState.WALK_UP;
        }
        else if (input.isDown(Input.Action.DOWN)) {
            dy += speed;
            animationState = AnimationState.WALK_DOWN;
        }
        else if (input.isDown(Input.Action.LEFT)) {
            dx -= speed;
            animationState = AnimationState.WALK_LEFT;
        }
        else if (input.isDown(Input.Action.RIGHT)) {
            dx += speed;
            animationState = AnimationState.WALK_RIGHT;
        }

        mailbox.moveRequests.add(new Movement(this, dx, dy, 0f));
        animate();

        Debugger.publish("PLAYER", new Debugger.DebugText("X: " + worldX + ", Y: " + worldY), 10, 90, Debugger.TYPE.INFO);
        Debugger.publish("HITBOX", new Debugger.DebugText("X: " + collisionBox.getBounds().x + ", Y: " + collisionBox.getBounds().y + ", Width: " + collisionBox.getBounds().width + ", Height: " + collisionBox.getBounds().height), 10, 120, Debugger.TYPE.INFO);
    }

    @Override
    public void animate() {
        animator.set(animations.get(animationState));
        spriteId = animator.update();
    }

    public void boostSpeed(float boost) {
        speed += boost;
    }
}
