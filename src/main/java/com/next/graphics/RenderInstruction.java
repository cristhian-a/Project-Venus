package com.next.graphics;

public record RenderInstruction(
        Layer layer,
        int worldX,
        int worldY,
        int spriteId
) {
}
