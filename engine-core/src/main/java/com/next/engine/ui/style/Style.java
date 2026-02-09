package com.next.engine.ui.style;

import com.next.engine.ui.AbstractNode;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class Style {
    private final AbstractNode owner;

    public Style(AbstractNode owner) {
        this.owner = owner;
        styleClasses.add(owner.getTypeName());
    }

    @Getter
    private String styleId;
    private final Set<String> styleClasses = new LinkedHashSet<>();
    // inline style to be done

    // engine maybe?

    private ComputedStyle computedStyle;

    public ComputedStyle getComputedStyle() {
        if (computedStyle == null) computedStyle = new ComputedStyle();
        if (dirty) return null;
        return computedStyle;
    }

    @Getter private boolean dirty = true;

    public void markDirty() {
        if (dirty) return;
        dirty = true;
        owner.markDirty();
    }

    public void markClean() {
        dirty = false;
    }

    public void setStyleId(String id) {
        this.styleId = id;
        markDirty();
    }

    public void addStyleClass(String clazz) {
        styleClasses.add(clazz);
        markDirty();
    }

    public boolean removeStyleClass(String clazz) {
        boolean did = styleClasses.remove(clazz);
        if (did) markDirty();
        return did;
    }

    public boolean hasStyleClass(String clazz) {
        return styleClasses.contains(clazz);
    }

    public boolean hasStyleClass(Collection<String> classes) {
        return !Collections.disjoint(styleClasses, classes);
    }

    public String getStyleIdInternal() {
        return styleId;
    }
}
