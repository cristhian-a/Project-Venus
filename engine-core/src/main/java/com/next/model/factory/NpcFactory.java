package com.next.model.factory;

import com.next.engine.animation.Animation;
import com.next.engine.animation.AnimationState;
import com.next.engine.data.Registry;
import com.next.model.NpcDummy;

import java.util.EnumMap;
import java.util.Map;

public class NpcFactory {

    public NpcDummy createDummy() {
        Map<AnimationState, Animation> animations = new EnumMap<>(AnimationState.class);

        int idle1 = Registry.textureIds.get("soldier-1.png");
        int walk1 = Registry.textureIds.get("soldier-2.png");
        int walk2 = Registry.textureIds.get("soldier-3.png");

        Animation walk = new Animation();
        walk.frameRate = 20;
        walk.frames = new int[] { walk1, walk2 };
        walk.loop = true;
        animations.put(AnimationState.WALK_UP, walk);
        animations.put(AnimationState.WALK_DOWN, walk);
        animations.put(AnimationState.WALK_LEFT, walk);
        animations.put(AnimationState.WALK_RIGHT, walk);

        Animation idle = new Animation();
        idle.frameRate = 0;
        idle.frames = new int[] { idle1 };
        idle.loop = false;
        animations.put(AnimationState.IDLE, idle);

        return new NpcDummy(22*16, 21*16, animations);
    }
}
