package com.next.graphics.awt;

import com.next.Game;
import com.next.graphics.Layer;
import com.next.model.Camera;
import com.next.system.AssetRegistry;
import com.next.system.Debugger;
import com.next.system.Settings.VideoSettings;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Renderer {
    private final UI ui;
    private final Game game;
    private final AssetRegistry assets;
    private final VideoSettings settings;
    private final TileRenderer tileRenderer;

    public Renderer(Game game, VideoSettings settings, AssetRegistry assets) {
        this.game = game;
        this.assets = assets;
        this.settings = settings;

        this.ui = new UI(assets, settings);
        this.tileRenderer = new TileRenderer(assets, game.getWorld());
    }

    public void render(Graphics2D g) {
        long start = System.nanoTime();
        Camera camera = game.getCamera();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        AffineTransform oldScale = g.getTransform();
        g.scale(settings.SCALE, settings.SCALE);

        render(Layer.BACKGROUND, g, camera);

        tileRenderer.render(g, camera);
        render(Layer.WORLD, g, camera);

        render(Layer.ACTORS, g, camera);

        g.setTransform(oldScale);   // de-scaling
        ui.render(g, camera);   // always last, damn it

//        render(Layer.UI, g, camera);
//        render(Layer.DEBUG, g, camera);

        long end = System.nanoTime();
        Debugger.publish("RENDER", new Debugger.DebugLong(end - start), 200, 30, Debugger.TYPE.INFO);
    }

    private void render(Layer layer, Graphics2D g, Camera camera) {
        var instructions = game.getRenderQueue().getLayer(layer);
        for (var instruction : instructions) {
            g.drawImage(
                    assets.getSpriteSheet("world").getSprite(instruction.spriteId()),
                    camera.worldToScreenX(instruction.worldX()),
                    camera.worldToScreenY(instruction.worldY()),
                    null
            );
        }
    }
}
