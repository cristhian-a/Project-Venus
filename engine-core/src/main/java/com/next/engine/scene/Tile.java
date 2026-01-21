package com.next.engine.scene;

public record Tile(
        int spriteId,
        int collisionLayer,
        int collisionMask
) {
}
