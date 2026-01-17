package com.next.game.model;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.model.Prop;
import com.next.engine.physics.CollisionBox;
import com.next.engine.physics.CollisionType;
import com.next.game.rules.Layers;

public class DestructibleTile extends Prop implements Damageable {

    private int health;
    private final int intactTileId;
    private final int brokenTileId;

    public DestructibleTile(
            int intactTileId, int brokenTileId, int health,
            float worldX, float worldY,
            CollisionBox collisionBox
    ) {
        var layer = Layers.WALL;
        var collisionType = CollisionType.SOLID;

        super(intactTileId, worldX, worldY, layer, collisionType, collisionBox);
        this.intactTileId = intactTileId;
        this.brokenTileId = brokenTileId;
        this.health = health;
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0)
            breakTile();
    }

    private void breakTile() {
        spriteId = brokenTileId;

        if (spriteId > 0) {
            this.disposed = false;
            collisionType = CollisionType.NONE;
        } else
            dispose();
   }

    @Override
    public void collectRender(RenderQueue queue) {
        queue.submit(Layer.WORLD, (int) worldX, (int) worldY, spriteId);
    }
}
