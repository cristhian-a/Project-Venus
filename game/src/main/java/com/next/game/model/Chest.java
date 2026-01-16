package com.next.game.model;

import com.next.engine.model.Prop;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionBox;
import com.next.engine.event.EventCollector;
import com.next.engine.physics.CollisionType;
import com.next.game.event.FinishGameEvent;
import com.next.game.rules.Layers;

public class Chest extends Prop {

    public Chest(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        this.spriteId = spriteId;
        this.layer = Layers.WALL;
        this.worldX = worldX;
        this.worldY = worldY;
        this.collisionType = collisionType;
        this.collisionBox = new CollisionBox(worldX, worldY, -8, -8, 16, 16);
    }

    @Override
    public void onCollision(Body other, EventCollector collector) {
        if (!(other instanceof Player)) return;
        collector.post(FinishGameEvent::new);
    }
}
