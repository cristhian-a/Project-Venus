package com.next.model.factory;

import com.next.engine.animation.Animation;
import com.next.engine.animation.AnimationState;
import com.next.engine.data.Registry;
import com.next.model.Mob;

import java.util.HashMap;
import java.util.Map;

public class MobFactory {

    public static Mob create(float x, float y) {

        int mob1 = Registry.textureIds.get("ghost-1.png");
        int mob2 = Registry.textureIds.get("ghost-2.png");
        int dead = Registry.textureIds.get("ghost-dead.png");

        Animation animation = new Animation();
        animation.loop = true;
        animation.frameRate = 10;
        animation.frames = new int[] { mob1, mob2 };

        Animation death = new Animation();
        death.loop = true;
        death.frameRate = 10;
        death.frames = new int[] { mob1, dead, mob1, dead, dead };

        Map<AnimationState, Animation> animations = new HashMap<>();
        animations.put(AnimationState.IDLE, animation);
        animations.put(AnimationState.DEAD, death);

        int pivot = 8;
        int offsetX = 3 - pivot;
        int offsetY = -pivot;
        return new Mob(animations, x + pivot, y + pivot, 10, 14, offsetX, offsetY);
    }
}
