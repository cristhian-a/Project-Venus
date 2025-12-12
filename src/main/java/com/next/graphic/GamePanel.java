package com.next.graphic;

import com.next.Game;
import com.next.io.InputReader;
import com.next.system.Settings.VideoSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class GamePanel extends JPanel implements ComponentListener {

    private final Game game;
    private final InputReader input;
    private final VideoSettings videoSettings;

    public GamePanel(Game game, InputReader input, VideoSettings videoSettings) {
        this.game = game;
        this.input = input;
        this.videoSettings = videoSettings;

        addKeyListener(input);
        addComponentListener(this);

        setPreferredSize(new Dimension(videoSettings.width, videoSettings.height));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocus();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render((Graphics2D) g);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        videoSettings.width = getWidth();
        videoSettings.height = getHeight();
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}
}
