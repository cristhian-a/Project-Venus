package com.next.engine.ui;

import com.next.engine.graphics.RenderQueue;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainer extends AbstractNode {

    protected final List<AbstractNode> children = new ArrayList<>();
    protected Layout layout;

    public void add(AbstractNode child) {
        children.add(child);
        child.setParent(this);
        markDirty();
    }

    public void remove(AbstractNode child) {
        children.remove(child);
        child.parent = null;
        markDirty();
    }

    public abstract Rect contentBounds();

    @Override
    public final void measure() {
        if (!dirty) return;
        for (int i = 0; i < children.size(); i++) {
            children.get(i).measure();
        }
        layout.calculatePreferredSize(this, children);
    }

    @Override
    public final void onLayout() {
        layout.arrange(this, children);

        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            child.updateLayout();
        }
    }

    @Override
    public void draw(RenderQueue queue) {
        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            child.draw(queue);
        }
    }
}
