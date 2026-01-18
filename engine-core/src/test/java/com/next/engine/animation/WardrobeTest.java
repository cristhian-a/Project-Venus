package com.next.engine.animation;

import org.junit.jupiter.api.Test;

public class WardrobeTest {

    @Test
    public void enumWardrobeTest() {
        enum AnimState { IDLE, ANIM_1, ANIM_2 }

        Costume cos_a = new StaticCostume(1);
        Costume cos_b = new StaticCostume(2);

        Wardrobe<AnimState> wardrobe = new EnumWardrobe<>(AnimState.class);
        wardrobe.add(AnimState.IDLE, cos_a);
        wardrobe.add(AnimState.ANIM_1, cos_b);

        Costume costume = wardrobe.get(AnimState.IDLE);
        assert costume == cos_a;

        costume = wardrobe.get(AnimState.ANIM_1);
        assert costume == cos_b;
    }
}
