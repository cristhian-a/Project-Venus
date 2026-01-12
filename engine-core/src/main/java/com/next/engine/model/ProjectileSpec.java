package com.next.engine.model;

import lombok.Builder;

@Builder
public record ProjectileSpec(
        float vx, float vy,
        HitboxSpec hitboxSpec,
        boolean penetrable
) {
}
