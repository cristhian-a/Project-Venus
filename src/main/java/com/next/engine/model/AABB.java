package com.next.engine.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class AABB {     // Axis-Aligned Bounding Box (just to remember the acronym)
    public float x;
    public float y;
    public float width;
    public float height;

    public boolean intersects(AABB other) {
        return x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y;
    }
}
