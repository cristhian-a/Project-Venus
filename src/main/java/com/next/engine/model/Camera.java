package com.next.engine.model;

import com.next.system.Debugger;
import lombok.Getter;

@Getter
public class Camera {
    private float x;
    private float y;

    private final int viewportWidth;
    private final int viewportHeight;

    public Camera(int viewportWidth, int viewportHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
    }

    public void follow(Actor actor) {
        follow(actor.worldX, actor.worldY);
        Debugger.publish("CAMERA", new Debugger.DebugText("X: " + x + ", Y: " + y), 10, 60, Debugger.TYPE.INFO);
    }

    public void follow(float targetX, float targetY) {
        x = targetX - (viewportWidth >> 1);
        y = targetY - (viewportHeight >> 1);
    }

    public int worldToScreenX(float worldX) {
        return Math.round(worldX - x);
    }

    public int worldToScreenY(float worldY) {
        return Math.round(worldY - y);
    }
}
