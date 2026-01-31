package com.next.engine.ui;

import java.util.List;

public final class HorizontalStackLayout implements Layout {

    private final float spacing;

    public HorizontalStackLayout(float spacing) {
        this.spacing = spacing;
    }

    private final Rect slot = new Rect();

    @Override
    public void arrange(AbstractContainer container, List<AbstractNode> children) {
        Rect content = container.contentBounds();
        float cursorX = content.x;

        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);

            float slotW = child.preferredSize.width;
            float slotH = content.height;
            slot.set(cursorX, content.y, slotW, slotH);

            float childX = LayoutUtils.alignX(slot, child.preferredSize.width, child.anchorX);
            float childY = LayoutUtils.alignY(slot, child.preferredSize.height, child.anchorY);

            child.localBounds.set(childX, childY, child.preferredSize.width, content.height);
            cursorX += slotW + spacing;
        }
    }
}
