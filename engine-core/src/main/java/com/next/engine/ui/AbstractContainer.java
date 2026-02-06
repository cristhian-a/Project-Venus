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
