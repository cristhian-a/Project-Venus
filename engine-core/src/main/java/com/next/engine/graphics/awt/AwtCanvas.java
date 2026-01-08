package com.next.engine.graphics.awt;

import com.next.engine.graphics.GamePanel;
import com.next.engine.system.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class AwtCanvas extends Canvas implements GamePanel, ComponentListener {

    private final Renderer renderer;
    private final KeyListener input;
    private final Settings.VideoSettings videoSettings;

    private final JFrame window;
    private BufferStrategy bufferStrategy;

    // just here so I can take a look later
//    private final StableValue<JFrame> jwindow = StableValue.of(); // late final initialization

    public AwtCanvas(Renderer renderer, Settings.VideoSettings videoSettings, KeyListener input) {
        this.videoSettings = videoSettings;
        this.renderer = renderer;
        this.input = input;

        setPreferredSize(new Dimension(videoSettings.WIDTH, videoSettings.HEIGHT));
        setBackground(Color.BLACK);

        window = new JFrame("Venus");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(this);
        window.pack();

        addComponentListener(this);
        createBufferStrategy(2);
        bufferStrategy = getBufferStrategy();
    }

    @Override
    public void openWindow() {
        SwingUtilities.invokeLater(() -> {
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            setFocusable(true);
            requestFocusInWindow();
            addKeyListener(input);
            setFocusTraversalKeysEnabled(false);
        });
    }

    @Override
    public void requestRender() {
        do {
            do {
                Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
                try {
                    g.clearRect(0, 0, getWidth(), getHeight());
                    renderer.render(g);
                } finally {
                    g.dispose();
                }
            } while (bufferStrategy.contentsRestored());

            bufferStrategy.show();
            Toolkit.getDefaultToolkit().sync();
        } while (bufferStrategy.contentsLost());
    }

    @Override
    public void componentResized(ComponentEvent e) {
        videoSettings.WIDTH = getWidth();
        videoSettings.HEIGHT = getHeight();

        videoSettings.UNSCALED_WIDTH = getWidth() / videoSettings.SCALE;
        videoSettings.UNSCALED_HEIGHT = getHeight() / videoSettings.SCALE;

        createBufferStrategy(2);
        bufferStrategy = getBufferStrategy();
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}
}
