package com.next.engine.ui;

import java.util.List;

public final class VerticalStackLayout implements Layout {

    private final float spacing;

    public VerticalStackLayout(float spacing) {
        this.spacing = spacing;
    }

    @Override
    public void arrange(AbstractContainer container, List<AbstractNode> children) {
        Rect content = container.contentBounds();
        float cursorY = content.y;

        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            child.localBounds.set(content.x, cursorY, content.width, child.localBounds.height);
            cursorY += child.localBounds.height + spacing;
        }
    }
}
