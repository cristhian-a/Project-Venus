package com.next.graphics.awt;

import com.next.Game;
import com.next.engine.data.Mailbox;
import com.next.graphics.Layer;
import com.next.graphics.RenderQueue;
import com.next.engine.model.Camera;
import com.next.system.AssetRegistry;
import com.next.system.Debugger;
import com.next.system.Settings.VideoSettings;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Renderer {
    private final UI ui;
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

        this.ui = new UI(assets, settings);
        this.tileRenderer = new TileRenderer(assets, game.getScene().world);
    }

    public void render(Graphics2D g) {
        long start = System.nanoTime();

        RenderQueue queue = mailbox.renderQueue;
        Camera camera = game.getCamera();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        AffineTransform oldScale = g.getTransform();
        g.scale(settings.SCALE, settings.SCALE);

//        render(Layer.BACKGROUND, queue, g, camera);

        tileRenderer.render(g, camera);
//        render(Layer.WORLD, queue, g, camera);

        render(Layer.ACTORS, queue, g, camera);

        g.setTransform(oldScale);   // de-scaling
        ui.render(g, queue, camera);   // always last, damn it

//        render(Layer.UI, queue, g, camera);
//        render(Layer.DEBUG, g, camera);

        queue.clear();

        long end = System.nanoTime();
        Debugger.publish("RENDER", new Debugger.DebugLong(end - start), 200, 30, Debugger.TYPE.INFO);
    }

    private void render(Layer layer, RenderQueue queue, Graphics2D g, Camera camera) {
        var instructions = queue.getLayer(layer);
        for (var instruction : instructions) {
            g.drawImage(
                    assets.getSpriteSheet("world").getSprite(instruction.getSpriteId()),
                    camera.worldToScreenX(instruction.getX()),
                    camera.worldToScreenY(instruction.getY()),
                    null
            );
        }
    }
}
