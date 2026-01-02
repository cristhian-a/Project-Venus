package com.next.model.factory;

import com.next.engine.animation.Animation;
import com.next.engine.data.Registry;
import com.next.model.ObjectFireCamp;

public class ObjectFactory {

    public static ObjectFireCamp create() {

        int fire1 = Registry.textureIds.get("firecamp-1.png");
        int fire2 = Registry.textureIds.get("firecamp-2.png");
        int fire3 = Registry.textureIds.get("firecamp-3.png");

        Animation animation = new Animation();
        animation.frameRate = 10;
        animation.frames = new int[] { fire2, fire1, fire3, fire1 };
        animation.loop = true;
        return new ObjectFireCamp(animation, 23 * 16, 21 * 16, 16, 16, 0, 0);
    }
}
