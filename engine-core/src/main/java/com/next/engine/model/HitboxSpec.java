package com.next.engine.model;

public record HitboxSpec(
        float offsetX, float offsetY, float width, float height,
        double durationSeconds,
        int damage,
        float knockback,
        int collisionLayer,
        boolean oneHitPerTarget,
        boolean followOwner
) {
}
