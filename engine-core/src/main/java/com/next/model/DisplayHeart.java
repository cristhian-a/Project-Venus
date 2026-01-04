package com.next.model;

import com.next.engine.model.Prop;
import com.next.engine.physics.CollisionType;

public class DisplayHeart extends Prop {

    private int[] sprites;

    private int depletion;

    public DisplayHeart(int fullHeartSprite, int halfHeartSprite, int emptyHeartSprite) {
        super(fullHeartSprite, 0f, 0f, CollisionType.NONE, null);

        sprites = new int[3];
        sprites[0] = fullHeartSprite;
        sprites[1] = halfHeartSprite;
        sprites[2] = emptyHeartSprite;
    }

    public void deplete() {
        depletion++;
        if (depletion >= sprites.length) {
            depletion = sprites.length - 1;
        }

        spriteId = sprites[depletion];
    }

    public void refill() {
        depletion--;
        if (depletion <= 0) {
            depletion = 0;
        }

        spriteId = sprites[depletion];
    }

    public void reset() {
        depletion = 0;
        spriteId = sprites[0];
    }
}
