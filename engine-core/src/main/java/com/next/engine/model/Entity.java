package com.next.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Entity {
    @Setter protected int id;
    protected boolean disposed;
    protected float worldX, worldY;

    /**
     * Marks this entity for disposal but does not do it immediately.
     */
    public final void dispose() {
        this.disposed = true;
    }
    public final boolean isDisposed() { return disposed; }

    public abstract void onDispose();
}
