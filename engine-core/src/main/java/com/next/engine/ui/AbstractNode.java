package com.next.engine.ui;

import com.next.engine.graphics.RenderQueue;

/// Base class for UI elements.
public abstract class AbstractNode {
    protected Rect localBounds = new Rect();
    protected Rect globalBounds = new Rect();
    protected Size preferredSize = new Size();
    protected boolean dirty = true;
    protected AbstractContainer parent;

    public final void maskAsDirty() {
        if (dirty) return;
        dirty = true;
        if (parent instanceof AbstractNode e) e.maskAsDirty();
    }

    public final boolean isDirty() { return dirty; }

    public final void setParent(AbstractContainer parent) { this.parent = parent; }

    public abstract void measure();

    public final void updateLayout() {
        if (!dirty) return;

        if (parent != null) {
            Rect parentBounds = parent.globalBounds;
            globalBounds.set(
                    parentBounds.x + localBounds.x,
                    parentBounds.y + localBounds.y,
                    localBounds.width,
                    localBounds.height
            );
        } else {
            globalBounds.set(localBounds);
        }

        layout();
        dirty = false;
    }

    public abstract void layout();
    public abstract void draw(RenderQueue queue);
}
