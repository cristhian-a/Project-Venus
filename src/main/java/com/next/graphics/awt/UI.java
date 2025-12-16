package com.next.graphics.awt;

import com.next.model.AABB;
import com.next.model.Camera;
import com.next.model.CollisionBox;
import com.next.system.AssetRegistry;
import com.next.system.Debugger;
import com.next.system.Settings.VideoSettings;

import java.awt.*;

public class UI {

    private final AssetRegistry assets;
    private final VideoSettings settings;

    public UI(AssetRegistry assets, VideoSettings settings) {
        this.assets = assets;
        this.settings = settings;
    }

    public void render(Graphics2D g, Camera camera) {
        g.setFont(assets.getFont("arial_30"));
        g.setColor(Color.GREEN);

        renderDebugInfo(g, camera);
    }

    public void renderDebugInfo(Graphics2D g, Camera camera) {
        var debug = Debugger.getRenderQueue();

        for (String key : debug.keySet()) {
            var renderInfo = debug.get(key);
            if (renderInfo.type() == Debugger.TYPE.INFO) {
                g.setColor(Color.GREEN);
                g.drawString(key + ": " + renderInfo.value().displayInfo(), renderInfo.x(), renderInfo.y());
            } else if (renderInfo.type() == Debugger.TYPE.COLLISION) {
                CollisionBox box = renderInfo.value().displayBox();
                AABB bounds = box.getBounds();

                int screenX = camera.worldToScreenX((int) bounds.x);
                int screenY = camera.worldToScreenY((int) bounds.y);
                var r = new Rectangle(screenX, screenY, (int) bounds.width, (int) bounds.height);

                var oldScale = g.getTransform();
                g.scale(settings.SCALE, settings.SCALE);  // TODO: get scale from the right place
                g.setColor(Color.RED);
                g.draw(r);
                g.setTransform(oldScale);   // de-scaling
            }
        }
    }
}
