package com.next.engine.graphics;

public record Sprite(
        int id,
        String name,
        int srcX, int srcY, int srcWidth, int srcHeight,
        float pivotX, float pivotY
) {
}
