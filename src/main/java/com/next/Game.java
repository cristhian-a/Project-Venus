package com.next;

import com.next.io.InputReader;
import com.next.system.Settings;

import java.awt.*;

public class Game {

    private final InputReader input;
    private final Settings settings;

    public Game(InputReader input, Settings settings) {
        this.input = input;
        this.settings = settings;
    }

    public void update(double delta) {
        // TODO: all the stuff goes here man
        if (input.isPressed(settings.controls.up))
            IO.println("Up");
        if (input.isPressed(settings.controls.down))
            IO.println("Down");
        if (input.isPressed(settings.controls.left))
            IO.println("Left");
        if (input.isPressed(settings.controls.right))
            IO.println("Right");
    }

    public void render(Graphics2D g) {
    }
}
