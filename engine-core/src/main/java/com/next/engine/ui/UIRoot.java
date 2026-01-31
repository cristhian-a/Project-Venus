package com.next.engine.ui;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import lombok.Getter;

import java.util.Objects;

public final class UIRoot extends AbstractContainer {

    @Getter
    private final FocusManager focusManager = new FocusManager(this);

    // content bounds for root should always have x and y equal to 0,
    // preventing layouts to break.
    private final Rect contentBounds = new Rect();

    public UIRoot(Rect bounds) {
        this(bounds, new AbsoluteLayout());
    }

    public UIRoot(Rect bounds, Layout layout) {
        Objects.requireNonNull(bounds, "Bounds for UIRoot cannot be null");
        Objects.requireNonNull(layout, "Layout for UIRoot cannot be null");

        super();
        this.localBounds.set(bounds);
        this.layout = layout;

        this.preferredSize.set(bounds.width, bounds.height);
        contentBounds.width = bounds.width;
        contentBounds.height = bounds.height;
    }

    @Override
    public Rect contentBounds() {
        return contentBounds;
    }

    @Override
    public void draw(RenderQueue queue) {
        super.draw(queue);

        if (focusManager.getFocused() instanceof AbstractNode an) {
            Rect r = an.globalBounds;
            queue.rectangle(Layer.UI_SCREEN, r.x-2, r.y-2, r.width+4, r.height+4, 0xffffffff);
        }
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

    @Override
    public void add(AbstractNode child) {
        super.add(child);
        focusManager.rebuild();
    }

    @Override
    public void remove(AbstractNode child) {
        super.remove(child);
        focusManager.rebuild();
    }
}
