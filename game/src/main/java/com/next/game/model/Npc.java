package com.next.game.model;

import com.next.engine.model.AnimatedActor;

public abstract class Npc extends AnimatedActor {

    @Override
    public void animate() {
        animator.set(animations.get(animationState));
        this.spriteId = animator.update();
    }
}
