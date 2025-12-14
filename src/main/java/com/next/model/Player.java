package com.next.model;

import com.next.system.Debugger;
import com.next.system.Input;
import com.next.system.Settings;

public class Player extends Entity {

    private int speed;

    public Player(int spriteId) {
        super(spriteId);
        setDefaultValues();
    }

    public void update(double delta, Input input) {
        super.update(delta);

        if (input.isDown(Input.Action.UP))
            worldY -= speed;
        if (input.isDown(Input.Action.DOWN))
            worldY += speed;
        if (input.isDown(Input.Action.LEFT))
            worldX -= speed;
        if (input.isDown(Input.Action.RIGHT))
            worldX += speed;

        Debugger.put("PLAYER", new Debugger.DebugText("X: " + worldX + ", Y: " + worldY));
    }

    public void setDefaultValues() {
        worldX = Settings.ORIGINAL_TILE_SIZE * 23;
        worldY = Settings.ORIGINAL_TILE_SIZE * 21;
        speed = 2;
    }
}
