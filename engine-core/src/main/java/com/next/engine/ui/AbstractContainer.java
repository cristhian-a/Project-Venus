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

    public abstract Rect contentBounds();
}
