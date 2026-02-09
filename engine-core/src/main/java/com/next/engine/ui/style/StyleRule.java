package com.next.engine.ui.style;

import java.util.List;

record StyleRule(Selector selector, List<StyleEntry> entries, int specificity, int order) {

    StyleRule(Selector selector, List<StyleEntry> entries, int order) {
        this(selector, List.copyOf(entries), selector.specificity(), order);
    }

    StyleRule(Selector selector, List<StyleEntry> entries, int specificity, int order) {
        this.order = order;
        this.selector = selector;
        this.specificity = specificity;
        this.entries = List.copyOf(entries);
    }

    void applyTo(ComputedStyle cs) {
        for (int i = 0; i < entries.size(); i++) {
            var e = entries.get(i);
            e.property().apply(cs, e.value());
        }
    }
}
