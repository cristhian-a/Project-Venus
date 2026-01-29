package com.next.engine.uix;

import java.util.List;

public interface LayoutStrategy {
    void calculatePreferredSize(UIContainer container, List<UINode> children);
    void arrange(UIContainer container, List<UINode> children);
}
