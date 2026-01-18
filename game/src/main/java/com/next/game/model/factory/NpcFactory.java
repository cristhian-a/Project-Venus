package com.next.game.model.factory;

import com.next.engine.animation.*;
import com.next.engine.data.Registry;
import com.next.game.animation.AnimationState;
import com.next.game.model.NpcDummy;

public class NpcFactory {

    public NpcDummy createDummy() {
        Wardrobe<AnimationState> wardrobe = new EnumWardrobe<>(AnimationState.class);

        int idle1 = Registry.textureIds.get("soldier-1.png");
        int walk1 = Registry.textureIds.get("soldier-2.png");
        int walk2 = Registry.textureIds.get("soldier-3.png");

        Animation walk = new Animation();
        walk.frameDuration = 0.3333f;
        walk.frames = new int[] { walk1, walk2 };
        walk.loop = true;

        Costume idleCostume = new StaticCostume(idle1);
        Costume walkCostume = new AnimatedCostume(walk);

        wardrobe.add(AnimationState.IDLE, idleCostume);
        wardrobe.add(AnimationState.WALK_UP, walkCostume);
        wardrobe.add(AnimationState.WALK_DOWN, walkCostume);
        wardrobe.add(AnimationState.WALK_LEFT, walkCostume);
        wardrobe.add(AnimationState.WALK_RIGHT, walkCostume);

        return new NpcDummy(22*16+8, 21*16+8, wardrobe);
    }
}
