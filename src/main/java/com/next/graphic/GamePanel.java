package com.next.graphic;

import com.next.Game;
import com.next.io.InputReader;
import com.next.system.Debugger;
import com.next.system.Settings.VideoSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class GamePanel extends JPanel implements ComponentListener {

    private final Game game;
    private final InputReader input;
    private final VideoSettings videoSettings;

    // DEBUG
    private final Font arial_30 = new Font("Arial", Font.PLAIN, 30);

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

        var g2 = (Graphics2D) g;
        g2.setFont(arial_30);
        g2.setColor(Color.MAGENTA);

        var debugData = Debugger.getPublishedData();
        if (debugData.containsKey("FPS"))
            g2.drawString("FPS: " + debugData.get("FPS").display(), 10, 30);
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
