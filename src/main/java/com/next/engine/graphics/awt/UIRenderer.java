package com.next.engine.graphics.awt;

import com.next.engine.graphics.*;
import com.next.engine.model.AABB;
import com.next.engine.model.Camera;
import com.next.engine.physics.CollisionBox;
import com.next.system.AssetRegistry;
import com.next.engine.system.Debugger;
import com.next.system.Settings.VideoSettings;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UIRenderer {

    private final AssetRegistry assets;
    private final VideoSettings settings;

    private final List<UIMessage> messages;

    public UIRenderer(AssetRegistry assets, VideoSettings settings) {
        this.assets = assets;
        this.settings = settings;

        messages = new ArrayList<>();
    }

    public void render(Graphics2D g, RenderQueue queue, int current, Camera camera) {
        int x = queue.x[current];
        int y = queue.y[current];

        if (queue.position[current] == RenderPosition.CENTERED) {
            x += settings.WIDTH / 2;
            y += settings.HEIGHT / 2;
        }

        if (queue.type[current] == RenderType.SPRITE) {
            renderSprite(g, queue.sprite[current], x, y);
        } else if (queue.type[current] == RenderType.TEXT) {
            if (queue.frames[current] > 0) {
                messages.add(new UIMessage(queue.message[current], queue.font[current], queue.color[current], x, y, queue.frames[current]));
            } else {
                renderText(g, queue.message[current], x, y, assets.getColor(queue.color[current]), assets.getFont(queue.font[current]));
            }
        } else if (queue.type[current] == RenderType.OVERLAY) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(x, y, settings.WIDTH, settings.HEIGHT);
        }
    }

    private void renderSprite(Graphics2D g, int sprite, int x, int y) {
        g.drawImage(
                assets.getSpriteSheet("world").getSprite(sprite),
                x,
                y,
                // TODO refactor: these are here just because we need to upscale the sprite
                16*settings.SCALE, 16*settings.SCALE,
                null
        );
    }

    public void renderMessages(Graphics2D g) {
        for (int i = 0; i < messages.size(); i++) {
            var message = messages.get(i);
            if (message.remainingFrames < 1) {
                messages.remove(message);
                continue;
            }

            renderText(g, message);
            message.remainingFrames--;
        }
    }

    private void renderText(Graphics2D g, UIMessage m) {
        renderText(g, m.text, m.x, m.y, assets.getColor(m.color), assets.getFont(m.font));
    }

    private void renderText(Graphics2D g, String text, int x, int y, Color color, Font font) {
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, x, y);
    }

    public void renderDebugInfo(Graphics2D g, Camera camera) {
        var debug = Debugger.getRenderQueue();
        g.setFont(assets.getFont("arial_30"));
        g.setColor(Color.GREEN);

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
