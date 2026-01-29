package com.next.engine.ui;

public final class LayoutUtils {
    private LayoutUtils() {}

    public static float alignX(Rect container, float width, Align align) {
        return switch (align) {
            case START  -> container.x;
            case CENTER -> container.centerX() - (width / 2f);
            case END    -> container.x + container.width - width;
        };
    }

    public static float alignY(Rect container, float height, Align align) {
        return switch (align) {
            case START  -> container.y;
            case CENTER -> container.centerY() - (height / 2f);
            case END    -> container.y + container.height - height;
        };
    }
}
