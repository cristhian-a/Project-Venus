package com.next.model;

import com.next.system.Debugger;
import lombok.Getter;

@Getter
public class Camera {
    private int x;
    private int y;

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

    public void follow(int targetX, int targetY) {
        x = targetX - (viewportWidth >> 1);
        y = targetY - (viewportHeight >> 1);
    }

    public int worldToScreenX(int worldX) {
        return worldX - x;
    }

    public int worldToScreenY(int worldY) {
        return worldY - y;
    }
}
