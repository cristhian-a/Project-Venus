package com.next.model;

import com.next.engine.data.Mailbox;
import com.next.engine.model.AnimatedActor;

public class Npc extends AnimatedActor {

    @Override
    public void update(double delta, Mailbox mailbox) {

    }

    @Override
    public void animate() {
        animator.set(animations.get(animationState));
        this.spriteId = animator.update();
    }
}
