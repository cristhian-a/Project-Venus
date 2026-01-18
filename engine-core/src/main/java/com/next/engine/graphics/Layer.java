package com.next.engine.graphics;

public enum Layer {
    BACKGROUND(     RenderSpace.WORLD,true),
    WORLD(          RenderSpace.WORLD,true),
    ACTORS(         RenderSpace.WORLD,true),
    PARTICLES(      RenderSpace.WORLD,true),
    LIGHTS(         RenderSpace.WORLD,true),
    UI_WORLD(       RenderSpace.SCREEN,true),
    UI_SCREEN(      RenderSpace.SCREEN,false),
    UI_SCR_SCALED(  RenderSpace.SCREEN,true),   // TODO this is the lazy solution,as I have to review this later anyway
    DEBUG_WORLD(    RenderSpace.WORLD,true),
    DEBUG_SCREEN(   RenderSpace.SCREEN,false);

    public final RenderSpace space;
    public final boolean scaled;

    Layer(RenderSpace space, boolean scaled) {
        this.space = space;
        this.scaled = scaled;
    }
}
