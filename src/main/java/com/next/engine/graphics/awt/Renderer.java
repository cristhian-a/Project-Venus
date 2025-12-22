package com.next.engine.graphics.awt;

import com.next.Game;
import com.next.engine.data.Mailbox;
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
        AffineTransform newScale = g.getTransform();

        // RENDERING START (preserve order)
        tileRenderer.render(g, camera); // First, rendering the world (check if this will stay like that)

        for (int i = 0; i < queue.size(); i++) {
            switch (queue.layer[i]) {
                case BACKGROUND -> {}
                case WORLD -> {}
                case ACTORS -> {
                    g.setTransform(newScale);   // up-scaling
                    renderSprite(queue, i, g, camera);
                }
                case UI -> {
                    g.setTransform(oldScale);   // de-scaling
                    uiRenderer.render(g, queue, i, camera);
                }
                case DEBUG -> {}
            }
        }

        g.setTransform(oldScale);
        uiRenderer.renderMessages(g);
        uiRenderer.renderDebugInfo(g, camera);

//        render(Layer.BACKGROUND, queue, g, camera);
//        render(Layer.WORLD, queue, g, camera);

//        render(Layer.ACTORS, queue, g, camera);

//        g.setTransform(oldScale);   // de-scaling
//        uiRenderer.render(g, queue, camera);   // always last, damn it

//        render(Layer.UI, queue, g, camera);
//        render(Layer.DEBUG, g, camera);

//        queue.clear();
        long end = System.nanoTime();
        Debugger.publish("RENDER", new Debugger.DebugLong(end - start), 200, 30, Debugger.TYPE.INFO);
    }

    private void renderSprite(RenderQueue queue, int current, Graphics2D g, Camera camera) {
        g.drawImage(
                assets.getSpriteSheet("world").getSprite(queue.sprite[current]),
                camera.worldToScreenX(queue.x[current]),
                camera.worldToScreenY(queue.y[current]),
                null
        );
    }
}
