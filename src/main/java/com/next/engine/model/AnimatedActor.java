package com.next.engine.model;

import com.next.engine.animation.Animation;
import com.next.engine.animation.AnimationState;
import com.next.engine.animation.Animator;

import java.util.EnumMap;
import java.util.Map;

/**
 * Actors that can be animated
 */
public abstract class AnimatedActor extends Actor {
    protected AnimationState animationState;
    protected Animator animator = new Animator();
    protected Map<AnimationState, Animation> animations = new EnumMap<>(AnimationState.class);

    public abstract void animate();
}
