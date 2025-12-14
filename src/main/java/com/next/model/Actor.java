package com.next.model;

import com.next.graphics.RenderData;

public abstract class Actor {
    protected int spriteId;
    protected int x;
    protected int y;

    public RenderData getRenderState() {
        return new RenderData(x, y, spriteId);
    }
}
