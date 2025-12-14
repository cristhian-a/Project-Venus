package com.next.graphics.awt;

import com.next.Game;
import com.next.graphics.RenderData;
import com.next.system.Debugger;

import java.awt.*;
import java.io.IOException;

public class Renderer implements Renderable {
    private final UI ui;
    private final Game game;

    private SpriteSheet spritesheet;

    public Renderer(Game game) {
        this.game = game;
        this.ui = new UI();

        try {
            this.spritesheet = new SpriteSheet("/sprites/spritesheet.png", 16, 16, 4);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(Graphics2D g) {
        long start = System.nanoTime();

        var whatToRender = game.getRenderBuffer();
        for (var entry : whatToRender.entrySet()) {
            RenderData state = entry.getValue();
            g.drawImage(spritesheet.getSprite(state.spriteId()), state.x(), state.y(), null);
        }

        ui.render(g);   // always last damn it

        long end = System.nanoTime();
        Debugger.put("RENDER", new Debugger.DebugLong(end - start));
    }
}
