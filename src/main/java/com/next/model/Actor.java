package com.next.model;

import com.next.graphics.RenderData;

public abstract class Actor {
    protected int spriteId;
    protected int worldX;
    protected int worldY;

    public RenderData getRenderState() {
        return new RenderData(worldX, worldY, spriteId);
    }
}
