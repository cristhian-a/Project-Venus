package com.next.game.model.factory;

import com.next.engine.model.Light;
import com.next.game.util.Lights;

public class LightFactory {

    public static Light create(float worldX, float worldY) {
        return new Light(Lights.NORMAL_LIGHT, worldX, worldY, 32f, 1f);
    }
}
