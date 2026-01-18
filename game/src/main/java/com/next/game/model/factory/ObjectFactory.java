package com.next.game.model.factory;

import com.next.engine.animation.Animation;
import com.next.engine.data.Registry;
import com.next.game.model.ObjectFireCamp;

public class ObjectFactory {

    public static ObjectFireCamp create() {

        int fire1 = Registry.textureIds.get("firecamp-1.png");
        int fire2 = Registry.textureIds.get("firecamp-2.png");
        int fire3 = Registry.textureIds.get("firecamp-3.png");

        Animation animation = new Animation();
        animation.frameDuration = 0.1666f;
        animation.frames = new int[] { fire2, fire1, fire3, fire1 };
        animation.loop = true;
        int offset = 8;
        int x = 23 * 16 + offset;
        int y = 21 * 16 + offset;
        return new ObjectFireCamp(animation, x, y, 16, 16, -offset, -offset);
    }
}
