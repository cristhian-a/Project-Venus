package com.next.engine.ui.test.node;

import java.util.List;

public interface LayoutStrategy {
    void calculatePreferredSize(UINodeContainer container, List<UINode> children);
    void arrange(UINodeContainer container, List<UINode> children);
}
