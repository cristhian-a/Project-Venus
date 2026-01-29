package com.next.engine.ui;

import java.util.List;

public final class AbsoluteLayout implements Layout {

    @Override
    public void arrange(AbstractContainer container, List<AbstractNode> children) {
        var parentBounds = container.contentBounds();
        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            child.globalBounds.set(
                    parentBounds.x + child.localBounds.x,
                    parentBounds.y + child.localBounds.y,
                    child.localBounds.width,
                    child.localBounds.height
            );
        }
    }
}
