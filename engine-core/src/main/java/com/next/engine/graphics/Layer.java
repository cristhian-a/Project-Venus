package com.next.engine.graphics;

public enum Layer {
    BACKGROUND(RenderSpace.WORLD),
    WORLD(RenderSpace.WORLD),
    ACTORS(RenderSpace.WORLD),
    LIGHTS(RenderSpace.WORLD),
    UI_WORLD(RenderSpace.UI_WORLD),
    UI_SCREEN(RenderSpace.SCREEN),
    DEBUG_WORLD(RenderSpace.WORLD),
    DEBUG_SCREEN(RenderSpace.SCREEN);

    public final RenderSpace space;

    Layer(RenderSpace space) {
        this.space = space;
    }
}
