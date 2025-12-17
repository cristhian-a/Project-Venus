package com.next.graphics;

public record RenderRequest(
        Layer layer,
        int worldX,
        int worldY,
        int spriteId
) {
}
