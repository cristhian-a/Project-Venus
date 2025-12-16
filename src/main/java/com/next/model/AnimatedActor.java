package com.next.model;

import com.next.core.Animation;
import com.next.core.AnimationState;
import com.next.core.Animator;

import java.util.EnumMap;

/**
 * Actors that can be animated
 */
public abstract class AnimatedActor extends Actor {
    protected AnimationState animationState;
    protected Animator animator = new Animator();
    protected EnumMap<AnimationState, Animation> animations = new EnumMap<>(AnimationState.class);

    public abstract void animate();
}
