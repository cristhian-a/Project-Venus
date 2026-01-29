package com.next.engine.ui;

import com.next.engine.data.Registry;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.graphics.TextFont;
import lombok.Setter;

public final class TextBlock extends AbstractNode {

    private final String fontId;
    private final TextFont font;
    private final int color;

    private String text;
    private String[] lines = new String[0];
    @Setter private float lineSpacing = 0f;

    public TextBlock(String text, String fontId, int color) {
        this.fontId = fontId;
        this.color = color;
        this.text = text;

        this.font = Registry.fonts.get(fontId);
    }

    private void recalculateBounds() {
        lines = text.split("\n");

        float maxWidth = 0f;
        for (String line : lines) maxWidth = Math.max(maxWidth, font.measureWidth(line));

        float height = lines.length * font.getLineHeight()
                + (lines.length - 1) * lineSpacing;

        localBounds.set(0, 0, maxWidth, height);
        maskAsDirty();
    }

    public void setText(String text) {
        this.text = text;
        recalculateBounds();
    }

    @Override
    public void layout() {
    }

    private float x, y; // transformed coordinates

    @Override
    public void measure() {
        var parentContent = parent.contentBounds();
        x = parentContent.x;
        y = parentContent.y + font.getAscent();
    }

    @Override
    public void draw(RenderQueue queue) {
        float yy = globalBounds.y;

        for (String line : lines) {
            queue.submit(
                    Layer.UI_SCREEN,
                    line,
                    fontId,
                    color,
                    globalBounds.x, yy + font.getAscent(),
                    RenderPosition.AXIS,
                    0
            );

            yy += font.getLineHeight() + lineSpacing;
        }
    }
}
