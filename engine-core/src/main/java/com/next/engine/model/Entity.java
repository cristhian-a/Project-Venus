package com.next.engine.model;

import com.next.engine.scene.SceneContext;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Entity {
    @Setter protected int id;
    protected boolean disposed;
    protected float worldX, worldY;
    @Setter protected SceneContext context;

    /**
     * Marks this entity for disposal but does not do it immediately.
     */
    public final void dispose() {
        this.disposed = true;
    }
    public final boolean isDisposed() { return disposed; }

    public abstract void onDispose();
}
