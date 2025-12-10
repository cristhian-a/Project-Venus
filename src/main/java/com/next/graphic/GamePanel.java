package com.next.graphic;

import com.next.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class GamePanel extends JPanel implements ComponentListener {

    private final Game game;

    private final int SCALE = 4;
    private final int MAX_SCREEN_COL = 16;
    private final int MAX_SCREEN_ROW = 12;
    private final int ORIGINAL_TILE_SIZE = 16;  // 16x16

    private final int TILE_SIZE;
    private final int WIDTH;
    private final int HEIGHT;

    public GamePanel(Game game) {
        this.game = game;
        addComponentListener(this);

        TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
        WIDTH = TILE_SIZE * MAX_SCREEN_COL;
        HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render((Graphics2D) g);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        int newWidth = getWidth();
        int newHeight = getHeight();

        IO.println("Width: " + newWidth + "; Height: " + newHeight);
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}
}
