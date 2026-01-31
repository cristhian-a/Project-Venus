package com.next.engine.ui;

import java.util.List;

public final class AbsoluteLayout implements Layout {

//    @Override
//    public void calculatePreferredSize(AbstractContainer container, List<AbstractNode> children) {
//        float w = 0, h = 0;
//        for (int i = 0; i < children.size(); i++) {
//            var child = children.get(i);
//            w = Math.max(w, child.preferredSize.width);
//            h = Math.max(h, child.preferredSize.height);
//        }
//        container.preferredSize.set(w, h);
//    }

    @Override
    public void arrange(AbstractContainer container, List<AbstractNode> children) {
        var content = container.contentBounds();

        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            if (child.localBounds.x > 0 || child.localBounds.y > 0) continue;

            float x = LayoutUtils.alignX(content, child.preferredSize.width, child.anchorX);
            float y = LayoutUtils.alignY(content, child.preferredSize.height, child.anchorY);

            child.localBounds.set(
                    x, y,
                    child.preferredSize.width, child.preferredSize.height
            );
        }
    }
}
