package com.next.graphics.awt;

import com.next.system.Debugger;

import java.awt.*;

public class UI implements Renderable {

    // DEBUG
    private final Font arial_30 = new Font("Arial", Font.PLAIN, 30);

    @Override
    public void render(Graphics2D g) {
        g.setFont(arial_30);
        g.setColor(Color.MAGENTA);

        var debugData = Debugger.getPublishedData();
        if (debugData.containsKey("FPS"))
            g.drawString("FPS: " + debugData.get("FPS").display(), 10, 30);
    }
}
