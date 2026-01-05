package com.next.engine.graphics.awt;

import com.next.engine.Director;
import com.next.engine.data.Mailbox;
import com.next.engine.data.Registry;
import com.next.engine.event.WorldTransitionEvent;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.model.Camera;
import com.next.engine.system.Debugger;
import com.next.engine.system.Settings.VideoSettings;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Renderer {
    private final UIRenderer uiRenderer;
    private final Director director;
    private final Mailbox mailbox;
    private final VideoSettings settings;
    private final TileRenderer tileRenderer;
    private final LightningRenderer lightningRenderer;

    public Renderer(Director director, Mailbox mailbox, VideoSettings settings) {
        this.director = director;
        this.mailbox = mailbox;
        this.settings = settings;

        this.uiRenderer = new UIRenderer(settings);
        this.tileRenderer = new TileRenderer();
        this.lightningRenderer = new LightningRenderer(settings);
    }

    // Event Handler    // TODO probably we shouldn't solve it like this
    public void onWorldTransition(WorldTransitionEvent event) {
        tileRenderer.setWorld(event.world());
    }

    public void render(Graphics2D g) {
        long start = System.nanoTime();

        RenderQueue queue = mailbox.receiveRender();
        Camera camera = director.getCamera();

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

        // 3.5 Lightning (black magic)
        lightningRenderer.render(g, camera, queue.getBucket(Layer.LIGHTS));

        // 4. UI - WORLD (health bars and stuff like that)
        var uiWorldLayer = queue.getBucket(Layer.UI_WORLD);
        uiRenderer.renderSpriteTable(g, uiWorldLayer.sprites);
        uiRenderer.renderRectangleTable(g, uiWorldLayer.rectangles);
        uiRenderer.renderFilledRectangleTable(g, uiWorldLayer.filledRectangles);
        uiRenderer.renderFilledRoundRectangleTable(g, uiWorldLayer.filledRoundRects);
        uiRenderer.renderRoundedStrokeRectTable(g, uiWorldLayer.roundedStrokeRectTable);
        uiRenderer.renderTextTable(g, uiWorldLayer.texts);
        uiRenderer.renderMessages(g);

        // 5. UI - SCREEN (Hud and stuff like that)
        var uiLayer = queue.getBucket(Layer.UI_SCREEN);
        uiRenderer.renderSpriteTable(g, uiLayer.sprites);
        g.setTransform(oldScale);
        uiRenderer.renderRectangleTable(g, uiLayer.rectangles);
        uiRenderer.renderFilledRectangleTable(g, uiLayer.filledRectangles);
        uiRenderer.renderFilledRoundRectangleTable(g, uiLayer.filledRoundRects);
        uiRenderer.renderRoundedStrokeRectTable(g, uiLayer.roundedStrokeRectTable);
        renderOverlayTable(g, uiLayer.overlays);
        uiRenderer.renderTextTable(g, uiLayer.texts);
        uiRenderer.renderMessages(g);

        // 6. DEBUG
        uiRenderer.renderDebugInfo(g, camera);  // TODO also

        long end = System.nanoTime();
        Debugger.publish("RENDER", new Debugger.DebugLong(end - start), 200, 30, Debugger.TYPE.INFO);
    }

    private void renderSpriteTable(Graphics2D g, Camera camera, RenderQueue.SpriteTable table) {
        for (int i = 0; i < table.count; i++) {
            var sprite = Registry.sprites.get(table.spriteId[i]);
            g.drawImage(
                    sprite.texture(),
                    camera.worldToScreenX(table.x[i] - sprite.pivotX()),
                    camera.worldToScreenY(table.y[i] - sprite.pivotY()),
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
