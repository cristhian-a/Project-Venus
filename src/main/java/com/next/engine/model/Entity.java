package com.next.engine.model;

import lombok.Getter;

@Getter
public abstract class Entity {
    protected int id;
    protected boolean disposed;
    protected float worldX, worldY;

    /**
     * Marks this entity for disposal but does not do it immediately.
     */
    public void dispose() {
        this.disposed = true;
    }
}
