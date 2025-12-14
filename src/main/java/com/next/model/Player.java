package com.next.model;

import com.next.system.Input;

public class Player extends Entity {

    private int speed;

    public Player(int spriteId) {
        super(spriteId);

        speed = 5;
    }

    public void update(double delta, Input input) {
        super.update(delta);

        if (input.isDown(Input.Action.UP))
            y -= speed;
        if (input.isDown(Input.Action.DOWN))
            y += speed;
        if (input.isDown(Input.Action.LEFT))
            x -= speed;
        if (input.isDown(Input.Action.RIGHT))
            x += speed;
    }
}
