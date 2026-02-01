package com.next.engine.ui.widget;

import com.next.engine.data.Registry;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.graphics.Sprite;
import com.next.engine.ui.*;
import com.next.engine.ui.component.ActionComponent;

public final class ImageNode extends AbstractNode implements Focusable {

    private final Sprite texture;
    private final boolean focusable;

    private final Rect drawRect = new Rect();
    private boolean focused;

    public ImageNode(String textureName, boolean focusable) {
        int idx = Registry.textureIds.get(textureName);
        var texture = Registry.sprites[idx];
        if (texture == null) throw new IllegalArgumentException("Texture not found: " + textureName);

        this.focusable = focusable;
        this.texture = texture;
        super();
    }

    public ImageNode(Sprite texture, boolean focusable) {
        if (texture == null) throw new IllegalArgumentException("Texture cannot be null");
        this.focusable = focusable;
        this.texture = texture;
        super();
    }

    @Override
    public void measure() {
        // preferred size = natural image size (duh)
        preferredSize.set(texture.srcWidth(), texture.srcHeight());
        localBounds.width = preferredSize.width;
        localBounds.height = preferredSize.height;
    }

    @Override
    public void onLayout() {
        float cellW = globalBounds.width;
        float cellH = globalBounds.height;

        if (texture.srcWidth() == 0 || texture.srcHeight() == 0) {
            drawRect.set(0, 0, cellW, cellH);
            return;
        }

        float tw = texture.srcWidth();
        float th = texture.srcHeight();

        // scaling to fit while preserving the aspect ratio
        float scale = Math.min(cellW / tw, cellH / th);
        float dw = tw * scale;
        float dh = th * scale;

        // computing anchor-based position inside the cell
        Rect cellRect = new Rect(globalBounds.x, globalBounds.y, cellW, cellH);
        float dx = LayoutUtils.alignX(cellRect, dw, anchorX);
        float dy = LayoutUtils.alignY(cellRect, dh, anchorY);
        dx += Math.round(texture.pivotX()) + 1;
        dy += Math.round(texture.pivotY()) + 1;

        drawRect.set(dx, dy, dw, dh);
    }

    @Override
    public void draw(RenderQueue queue) {
        queue.submit(Layer.UI_SCREEN, drawRect.x, drawRect.y, texture.id());

        if (focused) {
            queue.rectangle(
                    Layer.UI_SCREEN, globalBounds.x - 1, globalBounds.y - 1,
                    drawRect.width + 2, drawRect.height + 2,
                    0xFFFFFFFF
            );
        }
    }

    @Override
    public boolean isFocusable() {
        return focusable;
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
        var action = getComponent(ActionComponent.class);
        if (action != null) action.fire(input);
    }
}
