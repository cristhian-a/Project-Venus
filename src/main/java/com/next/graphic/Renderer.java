package com.next.graphic;

import com.next.Game;

import javax.swing.*;

public class Renderer {
    private final Game game;
    private final GamePanel panel;

    private JFrame window;

    public Renderer(Game game, GamePanel panel) {
        this.game = game;
        this.panel = panel;
    }

    public void open() {
        window = new JFrame("Project Venus");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.add(panel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public void render() {
        panel.repaint();
    }
}
