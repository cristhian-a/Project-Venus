package com.next.engine.ui;

import com.next.engine.graphics.RenderQueue;

/// Represents a container for user interface elements with specified bounds and optional padding.
/// The Panel serves as a base class for arranging and drawing `UIElement` children within
/// its bounds. It implements the `UIElement` and `UIContainer` interfaces.
///
/// The panel manages child elements by maintaining a list in their insertion order. The children
/// are drawn in the same order during the rendering phase.
///
/// Subclasses may extend this class to provide custom rendering functionality, such as drawing
/// decorative backgrounds or frames before rendering its children.
///
/// The bounds and padding of the panel define its visual appearance and its content area:
/// 1. The bounds represent the outer dimensions of the panel.
/// 2. The content area is derived by applying the padding to the bounds.
public class Panel extends AbstractContainer {
    private final float padding;

    public Panel(Rect bounds, Layout layout, float padding) {
        this.localBounds = bounds;
        this.padding = padding;
        this.layout = layout;
    }

    private final Rect contentBounds = new Rect(0, 0, 0, 0);

    @Override
    public final Rect contentBounds() {
        return contentBounds;
    }

    @Override
    public void measure() {
        if (!dirty) return;

        globalBounds.inset(padding, contentBounds);
        for (int i = 0; i < children.size(); i++) {
            children.get(i).measure();
        }

        layout.calculatePreferredSize(this, children);
    }

    @Override
    public void layout() {
        if (!dirty) return;

        globalBounds.inset(padding, contentBounds); // TODO should use local, but for now our preferred size needs global
        layout.arrange(this, children);

        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            child.layout();
        }
        dirty = false;
    }

    /// Draws this panel's child elements in the order they were added.
    ///
    /// @param queue buffered renderer in which the draw commands will be submitted.
    ///
    /// @implSpec Subclasses may override to draw custom content, such as a background or a frame.
    /// If overriding, call `super.draw(queue)` to draw this panel's children. Invoke the `super`
    /// method after any custom drawing logic; this way you ensure the children are drawn over it.
    @Override
    public void draw(RenderQueue queue) {
        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            child.draw(queue);
        }
    }
}
