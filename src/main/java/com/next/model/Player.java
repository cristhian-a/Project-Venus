package com.next.model;

import com.next.system.Debugger;
import com.next.system.Input;
import com.next.system.Settings;
import lombok.Setter;

public class Player extends Entity {

    @Setter private int speed;

    public Player(int spriteId) {
        super(spriteId);

        worldX = Settings.TILE_SIZE * 23;
        worldY = Settings.TILE_SIZE * 21;
        speed = 1;

        collisionBox = new CollisionBox(3, 6, 10, 10);
        collisionBox.setSolid(true);
    }

    public void update(double delta, Input input, CollisionInspector collisions) {
        super.update(delta);

        float dx = 0;
        float dy = 0;

        if (input.isDown(Input.Action.UP))      dy -= speed;
        if (input.isDown(Input.Action.DOWN))    dy += speed;
        if (input.isDown(Input.Action.LEFT))    dx -= speed;
        if (input.isDown(Input.Action.RIGHT))   dx += speed;

        moveX(dx, collisions);
        moveY(dy, collisions);

        Debugger.publish("PLAYER", new Debugger.DebugText("X: " + worldX + ", Y: " + worldY), 10, 90, Debugger.TYPE.INFO);
        Debugger.publish("HITBOX", new Debugger.DebugText("X: " + collisionBox.getBounds().x + ", Y: " + collisionBox.getBounds().y + ", Width: " + collisionBox.getBounds().width + ", Height: " + collisionBox.getBounds().height), 10, 120, Debugger.TYPE.INFO);
    }
}
