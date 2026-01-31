package com.next.engine.ui;

import com.next.engine.data.Registry;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.graphics.TextFont;
import com.next.engine.ui.component.Action;
import com.next.engine.ui.component.ActionComponent;
import com.next.engine.ui.component.FocusStyleComponent;

public final class Button extends AbstractNode implements Focusable {
    private final int color;
    private final String text;
    private final String fontId;
    private final TextFont font;

    private boolean focused;
    private int backgroundColor;

    public Button(String text, String font, Action action) {
        this.font = Registry.fonts.get(font);
        this.fontId = font;
        this.text = text;
        this.color = 0xffffffff;

        var focusStyle = new FocusStyleComponent();
        backgroundColor = focusStyle.normalBackgroundColor;

        addComponent(focusStyle);
        addComponent(new ActionComponent(action));

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
    public void onLayout() {}

    @Override
    public void draw(RenderQueue queue) {
        queue.fillRect(Layer.DEBUG_SCREEN, globalBounds.x, globalBounds.y, globalBounds.width, globalBounds.height, backgroundColor);
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

        var focusStyle = getComponent(FocusStyleComponent.class);
        if (focusStyle != null) backgroundColor = focusStyle.focusedBackgroundColor;
    }

    @Override
    public void onBlur() {
        focused = false;
        markDirty();

        var focusStyle = getComponent(FocusStyleComponent.class);
        if (focusStyle != null) backgroundColor = focusStyle.normalBackgroundColor;
    }

    @Override
    public void onActivate(String input) {
        var action = getComponent(ActionComponent.class);
        if (action != null) action.fire(input);
    }
}
