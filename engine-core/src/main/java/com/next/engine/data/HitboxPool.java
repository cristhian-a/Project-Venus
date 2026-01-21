package com.next.engine.data;

import com.next.engine.model.Hitbox;

import java.util.ArrayDeque;
import java.util.Deque;

public final class HitboxPool {
    private static final Deque<Hitbox> pool = new ArrayDeque<>(100);

    public static Hitbox obtain() {
        if (pool.isEmpty()) {
            return new Hitbox();
        }
        return pool.pop();
    }

    public static void free(Hitbox hitbox) {
        pool.push(hitbox);
    }
}
