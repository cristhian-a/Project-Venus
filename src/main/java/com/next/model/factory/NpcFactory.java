package com.next.model.factory;

import com.next.engine.animation.Animation;
import com.next.engine.animation.AnimationState;
import com.next.model.NpcGhost;

import java.util.EnumMap;
import java.util.Map;

public class NpcFactory {

    public NpcGhost create() {
        Map<AnimationState, Animation> animations = new EnumMap<>(AnimationState.class);

        Animation walk = new Animation();
        walk.frameRate = 15;
        walk.frames = new int[] { 16, 17 };
        walk.loop = true;
        animations.put(AnimationState.WALK_UP, walk);
        animations.put(AnimationState.WALK_DOWN, walk);
        animations.put(AnimationState.WALK_LEFT, walk);
        animations.put(AnimationState.WALK_RIGHT, walk);

        Animation idle = new Animation();
        idle.frameRate = 0;
        idle.frames = new int[] { 16 };
        idle.loop = false;
        animations.put(AnimationState.IDLE, idle);

        return new NpcGhost(22*16, 21*16, animations);
    }
}
