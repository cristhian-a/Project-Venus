package com.next.game.model;

import com.next.engine.model.Prop;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionBox;
import com.next.engine.event.EventCollector;
import com.next.engine.physics.CollisionType;
import com.next.game.event.NoKeysEvent;
import com.next.game.event.DoorUnlockedEvent;
import com.next.game.rules.Layers;

public class Door extends Prop {

    public Door(int spriteId, int worldX, int worldY, CollisionType collisionType) {
        CollisionBox box = new CollisionBox(worldX, worldY, -8, -8, 16, 16);
        super(spriteId, worldX, worldY, collisionType, box);
        this.layer = Layers.WALL;
    }

    @Override
    public void onCollision(Body other, EventCollector collector) {
        if (other instanceof Player player) {
            var key = player.getInventory().get(Key.class);
            if (key.isPresent()) {
                collector.post( () -> new DoorUnlockedEvent(this, player));
            } else {
                collector.post(NoKeysEvent::new);
            }
        }
    }
}
