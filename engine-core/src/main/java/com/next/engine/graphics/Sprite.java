package com.next.engine.graphics;

import java.awt.image.BufferedImage;

public record Sprite(
        int id,
        String name,
        int srcX, int srcY, int srcWidth, int srcHeight,
        float pivotX, float pivotY,
        BufferedImage texture
) {
}
