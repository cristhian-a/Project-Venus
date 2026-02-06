package com.next.engine.ui;

import com.next.engine.graphics.RenderQueue;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainer extends AbstractNode {

    protected final List<AbstractNode> children = new ArrayList<>();
    protected Layout layout;

    protected AbstractContainer() {
        this.layout = new AbsoluteLayout();
        super();
    }

    public final void add(AbstractNode child) {
        children.add(child);
        child.setParent(this);
        child.markDirty();
        markDirty();
    }

    public final void add(List<? extends AbstractNode> children) {
        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            child.parent = this;
            child.markDirty();
        }
        this.children.addAll(children);
        markDirty();
    }

    public final void remove(AbstractNode child) {
        children.remove(child);
        child.parent = null;
        markDirty();
    }

    public final void removeAll() {
        for (int i = 0; i < children.size(); i++) {
            children.get(i).parent = null;
        }
        children.clear();
        markDirty();
    }

    public final boolean contains(AbstractNode node) {
        return children.contains(node);
    }

    public abstract Rect contentBounds();

    protected final List<AbstractNode> visibleChildren = new ArrayList<>();

    @Override
    public final void measure() {
        if (!dirty || !visible) return;
        visibleChildren.clear();

        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            if (child.visible) {
                // after measurement, the visible children list is stable, so we can use it in all methods
                // that happen after measurement. As measure is invoked before updateLayout in the root, it
                // is safe to use almost everytime after this loop's end.
                visibleChildren.add(child);
                child.measure();
            }
        }
        layout.calculatePreferredSize(this, visibleChildren);
    }

    @Override
    public final void onLayout() {
        layout.arrange(this, visibleChildren);

        for (int i = 0; i < visibleChildren.size(); i++) {
            var child = visibleChildren.get(i);
            child.updateLayout();
        }
    }

    @Override
    public void draw(RenderQueue queue) {
        if (!visible) return;

        for (int i = 0; i < visibleChildren.size(); i++) {
            var child = visibleChildren.get(i);
            child.draw(queue);
        }
    }
}
