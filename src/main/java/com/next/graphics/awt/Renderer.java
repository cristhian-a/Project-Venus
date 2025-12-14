package com.next.graphics.awt;

import com.next.Game;

import java.awt.*;

public class Renderer implements Renderable {
    private final UI ui;
    private final Game game;

    public Renderer(Game game) {
        this.game = game;
        this.ui = new UI();
    }

    @Override
    public void render(Graphics2D g) {
        ui.render(g);
    }
}
