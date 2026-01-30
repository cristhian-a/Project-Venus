package com.next.engine.ui;

import com.next.engine.graphics.RenderQueue;

import java.util.Objects;

public final class UIRoot extends AbstractContainer {

    public UIRoot(Rect bounds) {
        this(bounds, new AbsoluteLayout());
    }

    public UIRoot(Rect bounds, Layout layout) {
        Objects.requireNonNull(bounds, "Bounds for UIRoot cannot be null");
        Objects.requireNonNull(layout, "Layout for UIRoot cannot be null");

        super();
        this.localBounds = bounds;
        this.layout = layout;
    }

    @Override
    public Rect contentBounds() {
        return globalBounds;
    }

    /// Updates layout if needed and collect draw request from the children nodes of this container.
    /// Safe to call every frame.
    public void render(RenderQueue queue) {
        if (dirty) {
            measure();
            updateLayout();
        }
        draw(queue);
    }
}
