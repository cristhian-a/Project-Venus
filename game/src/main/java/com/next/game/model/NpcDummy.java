package com.next.game.model;

import com.next.game.visual.AnimationState;
import com.next.engine.animation.Costume;
import com.next.engine.animation.Dresser;
import com.next.engine.animation.Wardrobe;
import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionType;
import com.next.game.rules.Layers;
import lombok.Getter;
import lombok.Setter;

public class NpcDummy extends Npc {

    private final Dresser<AnimationState> costume;

    @Getter @Setter private boolean behave = true;
    private int movementFrames = 50;
    private int direction = 0;

    public NpcDummy(float worldX, float worldY, Wardrobe<AnimationState> animations) {
        this.costume = new Dresser<>(animations);
        costume.wear(AnimationState.IDLE);

        this.collisionType = CollisionType.SOLID;
        this.collisionBox = new CollisionBox(-5, -4, 10, 12);
        this.layer = Layers.NPC;
        this.collisionMask = Layers.PLAYER | Layers.WALL;

        setPosition(worldX, worldY);
    }

    @Override
    public Costume getCostume() {
        return costume;
    }

    @Override
    public void update(double delta) {
        if (behave) behave();

        costume.update(delta);
    }

    private void behave() {
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
        switch (direction) {
            case 0 -> costume.wear(AnimationState.IDLE);
            case 1 -> {
                dx += speed;
                costume.wear(AnimationState.WALK_RIGHT);
            }
            case 2 -> {
                dx -= speed;
                costume.wear(AnimationState.WALK_LEFT);
            }
            case 3 -> {
                dy -= speed;
                costume.wear(AnimationState.WALK_UP);
            }
            case 4 -> {
                dy += speed;
                costume.wear(AnimationState.WALK_DOWN);
            }
        }

        context.mailbox().motionQueue.submit(this.id, dx, dy, 0f);
    }

}
