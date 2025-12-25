package com.next.engine.graphics.awt;

import com.next.engine.graphics.*;
import com.next.engine.model.AABB;
import com.next.engine.model.Camera;
import com.next.engine.physics.CollisionBox;
import com.next.system.AssetRegistry;
import com.next.engine.system.Debugger;
import com.next.system.Settings.VideoSettings;
import com.next.util.Colors;
import com.next.util.Fonts;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class UIRenderer {

    private final AssetRegistry assets;
    private final VideoSettings settings;

    private final List<UIMessage> messages;
    private final Stroke collisionStroke = new BasicStroke(1);

    protected UIRenderer(AssetRegistry assets, VideoSettings settings) {
        this.assets = assets;
        this.settings = settings;

        messages = new ArrayList<>();
    }

    protected void renderMessages(Graphics2D g) {
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

    protected void renderText(Graphics2D g, UIMessage m) {
        renderText(g, m.text, m.x, m.y, assets.getColor(m.color), assets.getFont(m.font));
    }

    protected void renderText(Graphics2D g, String text, int x, int y, Color color, Font font) {
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, x, y);
    }

    protected void renderSpriteTable(Graphics2D g, RenderQueue.SpriteTable table) {
        for (int i = 0; i < table.count; i++) {
            g.drawImage(
                    assets.getSpriteSheet("world").getSprite(table.spriteId[i]),
                    table.x[i],
                    table.y[i],
                    null
            );
        }
    }

    protected void renderTextTable(Graphics2D g, RenderQueue.TextTable table) {
        for (int i = 0; i < table.count; i++) {
            int x = table.x[i];
            int y = table.y[i];

            if (table.positions[i] == RenderPosition.CENTERED) {
                x += settings.WIDTH / 2;
                y += settings.HEIGHT / 2;
            }

            if (table.frames[i] > 0) {
                messages.add(new UIMessage(table.message[i], table.font[i], table.colors[i], x, y, table.frames[i]));
            } else {
                renderText(g, table.message[i], x, y, assets.getColor(table.colors[i]), assets.getFont(table.font[i]));
            }
        }
    }

    protected void renderRectangleTable(Graphics2D g, RenderQueue.RectangleTable table) {
        for (int i = 0; i < table.count; i++) {
            g.setColor(new Color(table.colors[i], true));
            g.drawRect((int) table.x[i], (int) table.y[i], (int) table.width[i], (int) table.height[i]);
        }
    }

    protected void renderRoundedStrokeRectTable(Graphics2D g, RenderQueue.RoundedStrokeRectTable table) {
        for (int i = 0; i < table.count; i++) {
            g.setColor(new Color(table.colors[i], true));
            g.setStroke(new BasicStroke(table.thickness[i]));
            g.drawRoundRect(
                    (int) table.x[i],
                    (int) table.y[i],
                    (int) table.width[i],
                    (int) table.height[i],
                    table.arc[i],
                    table.arc[i]
            );
        }
    }

    protected void renderFilledRectangleTable(Graphics2D g, RenderQueue.FilledRectangleTable table) {
        for (int i = 0; i < table.count; i++) {
            g.setColor(new Color(table.colors[i], true));
            g.fillRect((int) table.x[i], (int) table.y[i], (int) table.width[i], (int) table.height[i]);
        }
    }

    protected void renderFilledRoundRectangleTable(Graphics2D g, RenderQueue.RoundedFilledRectTable table) {
        for (int i = 0; i < table.count; i++) {
            g.setColor(new Color(table.colors[i], true));
            g.fillRoundRect(
                    (int) table.x[i],
                    (int) table.y[i],
                    (int) table.width[i],
                    (int) table.height[i],
                    table.arc[i],
                    table.arc[i]
            );
        }
    }

    protected void renderDebugInfo(Graphics2D g, Camera camera) {
        var debug = Debugger.getRenderQueue();
        g.setFont(assets.getFont(Fonts.DEFAULT));
        g.setColor(assets.getColor(Colors.GREEN));
        g.setStroke(collisionStroke);

        for (String key : debug.keySet()) {
            var renderInfo = debug.get(key);
            if (renderInfo.type() == Debugger.TYPE.INFO) {
                g.drawString(key + ": " + renderInfo.value().displayInfo(), renderInfo.x(), renderInfo.y());
            } else if (renderInfo.type() == Debugger.TYPE.COLLISION) {
                CollisionBox box = renderInfo.value().displayBox();
                AABB bounds = box.getBounds();

                int screenX = camera.worldToScreenX((int) bounds.x);
                int screenY = camera.worldToScreenY((int) bounds.y);

                var oldScale = g.getTransform();
                g.scale(settings.SCALE, settings.SCALE);  // TODO: get scale from the right place
                g.setColor(Color.RED);

                g.drawRect(screenX, screenY, (int) bounds.width, (int) bounds.height);

                // returning to old config
                g.setTransform(oldScale);
                g.setColor(Color.GREEN);
            }
        }
    }
}
