package com.next.engine.ui;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainer extends AbstractNode {

    protected final List<AbstractNode> children = new ArrayList<>();
    protected Layout layout;

    public final void add(AbstractNode child) {
        children.add(child);
        child.setParent(this);
        maskAsDirty();
    }

    public final void remove(AbstractNode child) {
        children.remove(child);
        child.parent = null;
        maskAsDirty();
    }

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

    public abstract Rect contentBounds();
}
