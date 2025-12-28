package com.next.model.factory;

import com.next.engine.animation.Animation;
import com.next.model.ObjectFireCamp;

public class ObjectFactory {

    public static ObjectFireCamp create() {
        Animation animation = new Animation();
        animation.frameRate = 20;
        animation.frames = new int[] { 39 };
        animation.loop = true;
        return new ObjectFireCamp(animation, 23 * 16, 21 * 16, 16, 16, 0, 0);
    }
}
