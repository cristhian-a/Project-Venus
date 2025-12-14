package com.next.graphics.awt;

import com.next.Game;
import com.next.graphics.RenderData;
import com.next.system.AssetRegistry;
import com.next.system.Debugger;

import java.awt.*;

public class Renderer implements Renderable {
    private final UI ui;
    private final Game game;
    private final AssetRegistry assets;

    public Renderer(Game game, AssetRegistry assets) {
        this.game = game;
        this.assets = assets;
        this.ui = new UI(assets);
    }

    @Override
    public void render(Graphics2D g) {
        long start = System.nanoTime();

        var whatToRender = game.getRenderBuffer();
        for (var entry : whatToRender.entrySet()) {
            RenderData state = entry.getValue();
            g.drawImage(assets.getSpriteSheet("world").getSprite(state.spriteId()), state.x(), state.y(), null);
        }

        ui.render(g);   // always last damn it

        long end = System.nanoTime();
        Debugger.put("RENDER", new Debugger.DebugLong(end - start));
    }
}
