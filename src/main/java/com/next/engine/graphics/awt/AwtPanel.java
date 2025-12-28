package com.next.engine.graphics.awt;

import com.next.engine.graphics.GamePanel;
import com.next.system.Settings.VideoSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;

public class AwtPanel extends JPanel implements ComponentListener, GamePanel {
    private final KeyListener input;
    private final Renderer renderer;
    private final VideoSettings videoSettings;

    private JFrame window;

    public AwtPanel(KeyListener input, VideoSettings videoSettings, Renderer renderer) {
        this.input = input;
        this.renderer = renderer;
        this.videoSettings = videoSettings;

        addKeyListener(input);
        addComponentListener(this);

        setPreferredSize(new Dimension(videoSettings.WIDTH, videoSettings.HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocus();
    }

    @Override
    public void openWindow() {
        window = new JFrame("Project Venus");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.add(this);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    @Override
    public void requestRender() {
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.render((Graphics2D) g);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        videoSettings.WIDTH = getWidth();
        videoSettings.HEIGHT = getHeight();

        videoSettings.UNSCALED_WIDTH = getWidth() / videoSettings.SCALE;
        videoSettings.UNSCALED_HEIGHT = getHeight() / videoSettings.SCALE;
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}
}
