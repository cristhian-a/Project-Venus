package com.next.engine.ui;

import com.next.engine.data.Registry;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.graphics.TextFont;

public final class Button extends AbstractNode implements Focusable {
    private final int color;
    private final String text;
    private final String fontId;
    private final TextFont font;
    private final Runnable action;

    private boolean focused;

    public Button(String text, String font, Runnable action) {
        this.font = Registry.fonts.get(font);
        this.fontId = font;
        this.text = text;
        this.action = action;
        this.color = 0xffffffff;
    }

    @Override
    public void measure() {
        float w = font.measureWidth(text);
        float h = font.getLineHeight();

        preferredSize.set(w, h);
        localBounds.width = w;
        localBounds.height = h;

        offsetY = font.getAscent();
    }

    private float offsetY;

    @Override
    public void onLayout() {
    }

    @Override
    public void draw(RenderQueue queue) {
        int bg = focused ? 0xFF4444AA : 0xFF222222;
        queue.fillRect(Layer.DEBUG_SCREEN, globalBounds.x, globalBounds.y, globalBounds.width, globalBounds.height, bg);
//        int c = focused ? 0xFFFF0000 : color;
        queue.submit(Layer.DEBUG_SCREEN, text, fontId, color,
                globalBounds.x, globalBounds.y + offsetY,
                RenderPosition.AXIS, 0
        );
    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void onFocus() {
        focused = true;
        markDirty();
    }

    @Override
    public void onBlur() {
        focused = false;
        markDirty();
    }

    @Override
    public void onActivate(String input) {
        if (action != null) action.run();
    }
}
