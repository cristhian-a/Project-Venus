package com.next.game.model.factory;

import com.next.engine.animation.*;
import com.next.engine.data.Registry;
import com.next.game.animation.AnimationState;
import com.next.game.model.Mob;

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

        Costume idleCostume = new AnimatedCostume(animation);
        Costume deathCostume = new AnimatedCostume(death);

        Wardrobe<AnimationState> wardrobe = new EnumWardrobe<>(AnimationState.class);
        wardrobe.add(AnimationState.IDLE, idleCostume);
        wardrobe.add(AnimationState.DEAD, deathCostume);

        int pivot = 8;
        int offsetX = 3 - pivot;
        int offsetY = -pivot;
        return new Mob(wardrobe, x + pivot, y + pivot, 10, 14, offsetX, offsetY);
    }
}
