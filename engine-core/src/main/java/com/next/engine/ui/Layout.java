package com.next.engine.ui;

import java.util.List;

public interface Layout {
    default void calculatePreferredSize(AbstractContainer container, List<AbstractNode> children) {}
    void arrange(AbstractContainer container, List<AbstractNode> children);
}
