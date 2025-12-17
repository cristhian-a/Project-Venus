package com.next.graphics;

/**
 * A request to be pushed to the renderer pipeline.
 * @param layer
 * @param worldX
 * @param worldY
 * @param spriteId
 */
public record RenderRequest(
        Layer layer,
        int worldX,
        int worldY,
        int spriteId
) {
}
