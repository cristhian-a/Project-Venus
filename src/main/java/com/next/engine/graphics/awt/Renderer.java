package com.next.engine.graphics.awt;

import com.next.Game;
import com.next.engine.data.Mailbox;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.model.Camera;
import com.next.system.AssetRegistry;
import com.next.engine.system.Debugger;
import com.next.system.Settings.VideoSettings;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Renderer {
    private final UIRenderer uiRenderer;
    private final Game game;
    private final Mailbox mailbox;
    private final AssetRegistry assets;
    private final VideoSettings settings;
    private final TileRenderer tileRenderer;

    public Renderer(Game game, Mailbox mailbox, VideoSettings settings, AssetRegistry assets) {
        this.game = game;
        this.mailbox = mailbox;
        this.assets = assets;
        this.settings = settings;

        this.uiRenderer = new UIRenderer(assets, settings);
        this.tileRenderer = new TileRenderer(assets, game.getScene().world);
    }

    public void render(Graphics2D g) {
        long start = System.nanoTime();

        RenderQueue queue = mailbox.renderQueue;
        Camera camera = game.getCamera();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        AffineTransform oldScale = g.getTransform();
        g.scale(settings.SCALE, settings.SCALE);

        // RENDERING START (very important: preserve order)
        // 1. Background
        renderSpriteTable(g, camera, queue.getBucket(Layer.BACKGROUND).sprites);

        // 2. World
        tileRenderer.render(g, camera);
        renderSpriteTable(g, camera, queue.getBucket(Layer.WORLD).sprites);

        // 3. Actors
        renderSpriteTable(g, camera, queue.getBucket(Layer.ACTORS).sprites);

        // 4. UI
        uiRenderer.renderSpriteTable(g, queue.getBucket(Layer.UI).sprites);
        g.setTransform(oldScale);
        renderOverlayTable(g, queue.getBucket(Layer.UI).overlays);
        uiRenderer.renderTextTable(g, queue.getBucket(Layer.UI).texts);
        uiRenderer.renderMessages(g);

        // 5. DEBUG
        uiRenderer.renderDebugInfo(g, camera);  // TODO also

        long end = System.nanoTime();
        Debugger.publish("RENDER", new Debugger.DebugLong(end - start), 200, 30, Debugger.TYPE.INFO);
    }

    private void renderSpriteTable(Graphics2D g, Camera camera, RenderQueue.SpriteTable table) {
        for (int i = 0; i < table.count; i++) {
            g.drawImage(
                    assets.getSpriteSheet("world").getSprite(table.spriteId[i]),
                    camera.worldToScreenX(table.x[i]),
                    camera.worldToScreenY(table.y[i]),
                    null
            );
        }
    }

    private void renderTextTable(Graphics2D g, RenderQueue.TextTable table) {
        for (int i = 0; i < table.count; i++) {
            // TODO
        }
    }

    private void renderOverlayTable(Graphics2D g, RenderQueue.OverlayTable table) {
        for (int i = 0; i < table.count; i++) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(table.x[i], table.y[i], settings.WIDTH, settings.HEIGHT);
        }
    }
}
