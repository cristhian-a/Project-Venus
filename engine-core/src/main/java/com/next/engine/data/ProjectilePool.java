package com.next.engine.data;

import com.next.engine.model.Projectile;

import java.util.ArrayDeque;
import java.util.Deque;

public final class ProjectilePool {
    private static final Deque<Projectile> pool = new ArrayDeque<>(100);

    public static Projectile obtain() {
        if (pool.isEmpty()) {
            return new Projectile();
        }
        return pool.pop();
    }

    public static void free(Projectile projectile) {
        pool.push(projectile);
    }
}
