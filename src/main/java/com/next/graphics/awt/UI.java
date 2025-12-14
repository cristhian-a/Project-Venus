package com.next.graphics.awt;

import com.next.system.AssetRegistry;
import com.next.system.Debugger;

import java.awt.*;

public class UI implements Renderable {

    // DEBUG
    private final AssetRegistry assets;

    public UI(AssetRegistry assets) {
        this.assets = assets;
    }

    @Override
    public void render(Graphics2D g) {
        g.setFont(assets.getFont("arial_30"));
        g.setColor(Color.MAGENTA);

        var debugData = Debugger.getPublishedData();
        if (debugData.containsKey("FPS"))
            g.drawString("FPS: " + debugData.get("FPS").display(), 10, 30);
        if (debugData.containsKey("RENDER"))
            g.drawString("RENDER: " + debugData.get("RENDER").display(), 200, 30);
    }
}
