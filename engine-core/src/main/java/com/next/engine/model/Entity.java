package com.next.engine.model;

import com.next.engine.scene.Scene;
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
     * Marks this entity for disposal, but the disposal itself usually does not happen immediately. At the end of the
     * current frame, {@link Scene} will remove any entity that has been marked for disposal and will call
     * {@link #onDispose()} on each entity that has been disposed.
     */
    public final void dispose() {
        this.disposed = true;
    }
    public final boolean isDisposed() { return disposed; }

    /**
     * Performs cleanup or custom logic when the entity is being disposed. {@code onDispose()} is called by
     * {@link Scene#dismissDisposed()} during the end of the frame in which that {@code Entity}'s {@link #dispose()}
     * method is called.
     * <br/><br/>
     * This method should be overridden to define specific behavior needed during
     * the disposal of the entity, such as releasing resources, detaching from
     * other systems, or resetting state.
     * <br/><br/>
     * By default, this method does nothing.
     */
    public void onDispose() {}
}
