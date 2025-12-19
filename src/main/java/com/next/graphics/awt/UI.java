package com.next.graphics.awt;

import com.next.core.physics.AABB;
import com.next.graphics.Layer;
import com.next.graphics.RenderQueue;
import com.next.graphics.RenderRequest;
import com.next.model.Camera;
import com.next.core.physics.CollisionBox;
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

    public void render(Graphics2D g, RenderQueue queue, Camera camera) {
        var requests = queue.getLayer(Layer.UI);

        for (var r : requests) {
            int x = r.getX();
            int y = r.getY();

            if (r.getPosition() == RenderRequest.Position.CENTERED) {
                x += settings.WIDTH / 2;
                y += settings.HEIGHT / 2;
            }

            if (r.getType() == RenderRequest.Type.SPRITE) {
                renderSprite(g, r.getSpriteId(), x, y);
            } else if (r.getType() == RenderRequest.Type.TEXT) {
                renderText(g, r.getMessage(), x, y, Color.WHITE, assets.getFont("arial_30"));
            }
        }

        g.setFont(assets.getFont("arial_30"));
        g.setColor(Color.GREEN);
        renderDebugInfo(g, camera);
    }

    private void renderSprite(Graphics2D g, int sprite, int x, int y) {
        g.drawImage(
                assets.getSpriteSheet("world").getSprite(sprite),
                x,
                y,
                16*4, 16*4, // TODO refactor: these are here just because we need to upscale the sprite
                null
        );
    }

    private void renderText(Graphics2D g, String text, int x, int y, Color color, Font font) {
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, x, y);
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
