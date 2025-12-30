package com.next.engine.model;

import com.next.engine.system.Debugger;
import lombok.Getter;

@Getter
public class Camera {
    private float x;
    private float y;

    private final int viewportWidth;
    private final int viewportHeight;
    private final int followOffsetX;
    private final int followOffsetY;
    private final int scale;

    public Camera(int viewportWidth, int viewportHeight, int followOffsetX, int followOffsetY, int scale) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.followOffsetX = followOffsetX;
        this.followOffsetY = followOffsetY;
        this.scale = scale;
    }

    public void follow(Entity e) {
        follow(e.worldX, e.worldY);
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
