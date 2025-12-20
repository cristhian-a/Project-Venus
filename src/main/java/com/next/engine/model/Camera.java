package com.next.engine.model;

import com.next.system.Debugger;
import lombok.Getter;

@Getter
public class Camera {
    private float x;
    private float y;

    private final int viewportWidth;
    private final int viewportHeight;
    private final int followOffsetX;
    private final int followOffsetY;

    public Camera(int viewportWidth, int viewportHeight, int followOffsetX, int followOffsetY) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.followOffsetX = followOffsetX;
        this.followOffsetY = followOffsetY;
    }

    public void follow(Actor actor) {
        follow(actor.worldX, actor.worldY);
        Debugger.publish("CAMERA", new Debugger.DebugText("X: " + x + ", Y: " + y), 10, 60, Debugger.TYPE.INFO);
    }

    public void follow(float targetX, float targetY) {
        x = targetX - ((float) (viewportWidth - followOffsetX) / 2);
        y = targetY - ((float) (viewportHeight - followOffsetY) / 2);
    }

    public int worldToScreenX(float worldX) {
        return Math.round(worldX - x);
    }

    public int worldToScreenY(float worldY) {
        return Math.round(worldY - y);
    }
}
