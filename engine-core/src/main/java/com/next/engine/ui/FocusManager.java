package com.next.engine.ui;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public final class FocusManager {
    private final AbstractContainer root;
    private final List<Focusable> elements = new ArrayList<>();

    public FocusManager(AbstractContainer root) {
        this.root = root;
        rebuild();
    }

    @Getter private Focusable focused;
    private int focusedIndex = -1;

    private Focusable pendingFocus;

    public void requestFocus(Focusable focus) {
        pendingFocus = focus;
    }

    public void rebuild() {
        Focusable preserved = focused;
        elements.clear();
        collect(root);
        if (elements.isEmpty()) {
            focused = null;
            focusedIndex = -1;
            return;
        }

        if (pendingFocus != null) {
            setFocus(pendingFocus);
            pendingFocus = null;
            return;
        }

        if (preserved != null) {
            int i = elements.indexOf(preserved);
            if (i >= 0) {
                setFocus(i);
                return;
            }
        }

        setFocus(0);
    }

    public void collect(AbstractNode node) {
        if (node instanceof Focusable f && f.isFocusable()) elements.add(f);
        if (node instanceof AbstractContainer container) {
            for (int i = 0; i < container.children.size(); i++) {
                // Although I am an enemy of recursivity, I'll let the explicit recursive
                // call here for better readability.
                // The other solution is implementing a collect method in AbstractNode
                // and letting it resolve for its children.
                collect(container.children.get(i));
            }
        }
    }

    public void focusNext() {
        if (elements.isEmpty()) return;
        setFocus((focusedIndex + 1) % elements.size());
    }

    public void focusPrevious() {
        if (elements.isEmpty()) return;
        int n = elements.size();
        setFocus((focusedIndex + n - 1) % n);
    }

    public void setFocus(int index) {
        if (index < 0 || index >= elements.size()) return;

        Focusable f = elements.get(index);
        if (focused == f) return;
        if (focused != null) focused.onBlur();
        focused = f;
        focusedIndex = index;
        focused.onFocus();
    }

    public void setFocus(Focusable focus) {
        if (focus == null) return;
        int index = elements.indexOf(focus);
        setFocus(index);
    }

    public void activateFocused(String action) {
        if (focused != null) focused.onActivate(action);
    }

    public void focusDirection(float dx, float dy) {
        if (elements.isEmpty()) return;
        if (focused == null) {
            setFocus(0);
            return;
        }

        AbstractNode currentNode = (AbstractNode) focused;
        float cx = currentNode.globalBounds.x + currentNode.globalBounds.width / 2f;
        float cy = currentNode.globalBounds.y + currentNode.globalBounds.height / 2f;

        float bestScore = Float.POSITIVE_INFINITY;
        int bestIndex = -1;

        // normalize direction
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len == 0) return;
        dx /= len;
        dy /= len;

        for (int i = 0; i < elements.size(); i++) {
            Focusable candidate = elements.get(i);
            if (candidate == focused) continue;
            AbstractNode cNode = (AbstractNode) candidate;
            float tcx = cNode.globalBounds.x + cNode.globalBounds.width / 2f;
            float tcy = cNode.globalBounds.y + cNode.globalBounds.height / 2f;
            float vx = tcx - cx;
            float vy = tcy - cy;
            float vlen =  (float) Math.sqrt(vx * vx + vy * vy);
            if (vlen == 0) continue;
            // dot product to check angle alignment
            float dot = (vx * dx + vy * dy) / vlen; // cosine of an angle
            if (dot <= 0.3f) continue;  // threshold: only candidates closer than 30 degrees are considered
            // score combines angle and distance (lower better)
            float score = (1f - dot) * 1000 + vlen;
            if (score < bestScore) {
                bestScore = score;
                bestIndex = i;
            }
        }

        if (bestIndex >= 0) setFocus(bestIndex);
    }
}
