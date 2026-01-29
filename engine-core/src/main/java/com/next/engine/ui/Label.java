package com.next.engine.ui;

import com.next.engine.data.Registry;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.graphics.TextFont;

public final class Label extends AbstractNode {

    private final int color;
    private final Align alignX;
    private final Align alignY;
    private final String fontId;
    private final TextFont font;

    private String text;

    public Label(String text, String fontId, int color, Align alignX, Align alignY) {
        this.text = text;
        this.fontId = fontId;
        this.alignX = alignX;
        this.alignY = alignY;
        this.color = color;

        this.font = Registry.fonts.get(fontId);
        recalculateBounds();
    }

    private void recalculateBounds() {
        float w = font.measureWidth(text);
        float h = font.getLineHeight();
        localBounds.set(0, 0, w, h);
        maskAsDirty();
    }

    public void setText(String text) {
        this.text = text;
        recalculateBounds();
    }

    @Override
    public void layout() {
        measure();

        Rect area = this.parent.contentBounds();

        float x = LayoutUtils.alignX(area, localBounds.width, alignX);
        float y = LayoutUtils.alignY(area, localBounds.height, alignY) + font.getAscent();

        globalBounds.set(x, y, localBounds.width, localBounds.height);
    }

    @Override
    public void measure() {
        float w = font.measureWidth(text);
        float h = font.getLineHeight();

        preferredSize.set(w, h);
        localBounds.set(localBounds.x, localBounds.y + font.getAscent(), w, h);
    }

    @Override
    public void draw(RenderQueue queue) {
        queue.submit(
                Layer.UI_SCREEN,
                text,
                fontId,
                color,
                globalBounds.x, globalBounds.y,
                RenderPosition.AXIS,
                0
        );
    }
}
