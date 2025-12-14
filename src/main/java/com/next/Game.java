package com.next;

import com.next.io.InputReader;
import com.next.system.Input;
import com.next.system.Settings;

import java.awt.*;

public class Game {

    private final Input input;
    private final Settings settings;

    public Game(Input input, Settings settings) {
        this.input = input;
        this.settings = settings;
    }

    public void update(double delta) {
        // TODO: all the stuff goes here man
        if (input.isDown(Input.Action.UP))
            IO.println("Up");
        if (input.isReleased(Input.Action.DOWN))
            IO.println("Down");
        if (input.isPressed(Input.Action.LEFT))
            IO.println("Left");
        if (input.isPressed(Input.Action.RIGHT))
            IO.println("Right");
    }

    public void render(Graphics2D g) {
    }
}
