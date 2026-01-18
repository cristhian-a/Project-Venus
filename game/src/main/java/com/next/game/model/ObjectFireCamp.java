package com.next.game.model;

import com.next.engine.animation.*;
import com.next.engine.model.Actor;
import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionType;
import com.next.engine.scene.ParticleEmitter;
import com.next.game.visual.AnimationState;
import com.next.game.rules.Layers;

public class ObjectFireCamp extends Actor {

    private final Dresser<AnimationState> costume;
    private final ParticleEmitter smokeEmitter;

    public ObjectFireCamp(Animation animation, int x, int y, int width, int height, int offsetX, int offsetY,
                          ParticleEmitter smokeEmitter
    ) {
        Wardrobe<AnimationState> animations = new EnumWardrobe<>(AnimationState.class);
        Costume c = new AnimatedCostume(animation);
        animations.add(AnimationState.IDLE, c);
        costume = new Dresser<>(animations);
        costume.wear(AnimationState.IDLE);

        this.worldX = x;
        this.worldY = y;
        this.collisionType = CollisionType.SOLID;
        this.collisionBox = new CollisionBox(x, y, offsetX, offsetY, width, height);
        this.layer = Layers.WALL;
        this.collisionMask = 0;

        this.smokeEmitter = smokeEmitter;
    }

    @Override
    public Costume getCostume() {
        return costume;
    }

    @Override
    public void update(double delta) {
        costume.update(delta);
        smokeEmitter.update(delta, context::emitParticle);
    }

}
