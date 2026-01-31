package com.next.engine.ui;

public final class LayoutUtils {
    private LayoutUtils() {}

    public static float alignX(Rect container, float elementWidth, Align align) {
        return switch (align) {
            case START  -> container.x;
            case CENTER -> container.centerX() - (elementWidth / 2f);
            case END    -> container.x + container.width - elementWidth;
        };
    }

    public static float alignY(Rect container, float elementHeight, Align align) {
        return switch (align) {
            case START  -> container.y;
            case CENTER -> container.centerY() - (elementHeight / 2f);
            case END    -> container.y + container.height - elementHeight;
        };
    }
}
