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
        Debugger.put("CAMERA", new Debugger.DebugText("X: " + x + ", Y: " + y));
    }

    public void follow(int targetX, int targetY) {
        x = targetX - viewportWidth / 2;
        y = targetY - viewportHeight / 2;
    }

    public int worldToScreenX(int worldX) {
        return worldX - x;
    }

    public int worldToScreenY(int worldY) {
        return worldY - y;
    }
}
