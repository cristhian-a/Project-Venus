package com.next.engine.ui;

import com.next.engine.graphics.RenderQueue;

/// Base class for UI elements.
public abstract class AbstractNode {
    protected Rect localBounds = new Rect();
    protected Rect globalBounds = new Rect();
    protected Size preferredSize = new Size();
    protected boolean dirty = true;
    protected AbstractContainer parent;

    public final void markDirty() {
        if (dirty) return;
        dirty = true;
        if (parent != null) parent.markDirty();
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

        onLayout();
        dirty = false;
    }

    public abstract void onLayout();
    public abstract void draw(RenderQueue queue);

    // Anchoring
    protected Align anchorX = Align.START;
    protected Align anchorY = Align.START;

    public final void anchorX(Align align) {
        anchorX = align;
        markDirty();
    }

    public final void anchorY(Align align) {
        anchorY = align;
        markDirty();
    }

    public final void setAnchor(Align alignX, Align alignY) {
        anchorX = alignX;
        anchorY = alignY;
        markDirty();
    }
}
