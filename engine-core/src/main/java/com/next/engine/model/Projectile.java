package com.next.engine.model;

import com.next.engine.data.ProjectilePool;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import lombok.Getter;

public class Projectile extends Entity implements Updatable, Renderable {

    private int spriteId;
    private float vx, vy;
    private double ttl;

    @Getter private ProjectileSpec spec;
    private Hitbox hitbox;

    public void init(int spriteId, float worldX, float worldY, Hitbox hitbox, ProjectileSpec spec) {
        this.disposed = false;

        this.spriteId = spriteId;
        this.worldX = worldX;
        this.worldY = worldY;
        this.hitbox = hitbox;
        this.spec = spec;
        this.vx = spec.vx();
        this.vy = spec.vy();
        this.ttl = spec.hitboxSpec().durationSeconds();

        hitbox.init(this, worldX, worldY, spec.hitboxSpec());
    }

    @Override
    public void onDispose() {
        ProjectilePool.free(this);
        hitbox.dispose();
        hitbox = null;
        spec = null;
        vx = vy = 0;
    }

    @Override
    public void update(double delta) {
        worldX += (float) (vx * delta);
        worldY += (float) (vy * delta);

        ttl -= delta;
        if (ttl <= 0) dispose();

        hitbox.collisionBox.update(worldX + spec.hitboxSpec().offsetX(), worldY + spec.hitboxSpec().offsetY());
    }

    @Override
    public void collectRender(RenderQueue queue) {
        queue.submit(Layer.ACTORS, (int) worldX, (int) worldY, spriteId);
    }
}
