package com.next.graphics.awt;

import com.next.Game;
import com.next.graphics.RenderData;
import com.next.model.Camera;
import com.next.system.AssetRegistry;
import com.next.system.Debugger;
import com.next.system.Settings.VideoSettings;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Renderer {
    private final UI ui;
    private final Game game;
    private final World world;
    private final AssetRegistry assets;
    private final VideoSettings settings;

    public Renderer(Game game, VideoSettings settings, AssetRegistry assets) {
        this.game = game;
        this.assets = assets;
        this.settings = settings;

        this.ui = new UI(assets);
        this.world = new World(assets, game);
    }

    public void render(Graphics2D g) {
        long start = System.nanoTime();
        Camera camera = game.getCamera();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        AffineTransform oldScale = g.getTransform();
        g.scale(settings.SCALE, settings.SCALE);
        world.render(g, camera);    // always first, silly

        var whatToRender = game.getRenderBuffer();
        for (var entry : whatToRender.entrySet()) {
            RenderData state = entry.getValue();
            g.drawImage(
                    assets.getSpriteSheet("world").getSprite(state.spriteId()),
                    camera.worldToScreenX(state.worldX()),
                    camera.worldToScreenY(state.worldY()),
                    null
            );
        }

        g.setTransform(oldScale);   // de-scaling
        ui.render(g);   // always last, damn it

        long end = System.nanoTime();
        Debugger.put("RENDER", new Debugger.DebugLong(end - start));
    }
}
