package com.next.graphics.awt;

import com.next.system.AssetRegistry;
import com.next.system.Debugger;

import java.awt.*;

public class UI {

    private final AssetRegistry assets;

    public UI(AssetRegistry assets) {
        this.assets = assets;
    }

    public void render(Graphics2D g) {
        g.setFont(assets.getFont("arial_30"));
        g.setColor(Color.GREEN);

        var debugData = Debugger.getPublishedData();
        if (debugData.containsKey("FPS"))
            g.drawString("FPS: " + debugData.get("FPS").display(), 10, 30);
        if (debugData.containsKey("RENDER"))
            g.drawString("RENDER: " + debugData.get("RENDER").display(), 200, 30);
        if (debugData.containsKey("PLAYER"))
            g.drawString("PLAYER: " + debugData.get("PLAYER").display(), 10, 60);
        if (debugData.containsKey("CAMERA"))
            g.drawString("CAMERA: " + debugData.get("CAMERA").display(), 10, 90);
    }
}
