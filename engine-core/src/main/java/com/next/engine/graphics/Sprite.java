package com.next.engine.graphics;

public record Sprite(
        int id,
        String name,
        int srcX, int srcY, int srcX2, int srcY2,
        int srcWidth, int srcHeight,
        float pivotX, float pivotY
) {
}
