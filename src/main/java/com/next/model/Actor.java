package com.next.model;

import com.next.graphics.Layer;
import com.next.graphics.RenderInstruction;

public abstract class Actor {
    protected int spriteId;
    protected int worldX;
    protected int worldY;

    public RenderInstruction getRenderInstruction() {
        return new RenderInstruction(Layer.ACTORS, worldX, worldY, spriteId);
    }
}
