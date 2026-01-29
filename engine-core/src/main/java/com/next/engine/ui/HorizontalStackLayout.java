package com.next.engine.ui;

import java.util.List;

public final class HorizontalStackLayout implements Layout {

    private final float spacing;

    public HorizontalStackLayout(float spacing) {
        this.spacing = spacing;
    }

    @Override
    public void arrange(AbstractContainer container, List<AbstractNode> children) {
        Rect content = container.contentBounds();
        float cursorX = content.x;
        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            child.globalBounds.set(cursorX, content.y, child.localBounds.width, content.height);
            cursorX += child.localBounds.width + spacing;
        }
    }
}
