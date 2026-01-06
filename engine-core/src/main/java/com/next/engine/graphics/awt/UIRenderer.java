package com.next.engine.graphics.awt;

import com.next.engine.data.Registry;
import com.next.engine.graphics.*;
import com.next.engine.system.Settings.VideoSettings;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class UIRenderer {

    private final VideoSettings settings;

    private final List<UIMessage> messages;
    private final Stroke collisionStroke = new BasicStroke(1);

    private final Font debugFont = new Font("Arial", Font.PLAIN, 30);

    protected UIRenderer(VideoSettings settings) {
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
        renderText(g, m.text, m.x, m.y, Registry.colors.get(m.color), Registry.fonts.get(m.font), m.position);
    }

    protected void renderText(Graphics2D g, String text, int x, int y, Color color, Font font, RenderPosition position) {
        g.setColor(color);
        g.setFont(font);


        if (position == RenderPosition.CENTERED) {
            FontMetrics fm = g.getFontMetrics();

            int textWidth = fm.stringWidth(text);

            x += (settings.WIDTH - textWidth) / 2;
            y += ((settings.HEIGHT - fm.getHeight()) / 2) + fm.getAscent();
        }

        g.drawString(text, x, y);
    }

    protected void renderSpriteTable(Graphics2D g, RenderQueue.SpriteTable table) {
        for (int i = 0; i < table.count; i++) {
            g.drawImage(
                    Registry.sprites.get(table.spriteId[i]).texture(),
                    table.x[i],
                    table.y[i],
                    null
            );
        }
    }

    protected void renderTextTable(Graphics2D g, RenderQueue.TextTable table) {
        for (int i = 0; i < table.count; i++) {
            if (table.frames[i] > 0) {
                messages.add(new UIMessage(table.message[i], table.font[i], table.colors[i], table.x[i], table.y[i], table.positions[i], table.frames[i]));
            } else {
                renderText(g, table.message[i], table.x[i], table.y[i], Registry.colors.get(table.colors[i]), Registry.fonts.get(table.font[i]), table.positions[i]);
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

//    protected void renderDebugInfo(Graphics2D g, Camera camera) {
//        var debug = Debugger.getRenderQueue();
//        g.setFont(debugFont);
//        g.setColor(Color.GREEN);
//        g.setStroke(collisionStroke);
//
//        for (String key : debug.keySet()) {
//            var renderInfo = debug.get(key);
//            if (renderInfo.channel() == DebugChannel.INFO) {
//                g.drawString(key + ": " + renderInfo.value().displayInfo(), renderInfo.x(), renderInfo.y());
//            } else if (renderInfo.channel() == DebugChannel.COLLISION) {
//                CollisionBox box = renderInfo.value().displayBox();
//                AABB bounds = box.getBounds();
//
//                int screenX = camera.worldToScreenX((int) bounds.x);
//                int screenY = camera.worldToScreenY((int) bounds.y);
//
//                var oldScale = g.getTransform();
//                g.scale(settings.SCALE, settings.SCALE);  // TODO: get scale from the right place
//                g.setColor(Color.RED);
//
//                g.drawRect(screenX, screenY, (int) bounds.width, (int) bounds.height);
//
//                // returning to old config
//                g.setTransform(oldScale);
//                g.setColor(Color.GREEN);
//            }
//        }
//    }
}
