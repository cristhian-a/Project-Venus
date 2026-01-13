package com.next.game.model;

import com.next.engine.animation.Animation;
import com.next.engine.animation.AnimationState;
import com.next.engine.model.AnimatedActor;
import com.next.engine.physics.Body;
import com.next.engine.physics.CollisionBox;
import com.next.engine.event.EventCollector;
import com.next.engine.physics.CollisionType;
import com.next.game.rules.Layers;

import java.util.HashMap;

public class ObjectFireCamp extends AnimatedActor {

    public ObjectFireCamp(Animation animation, int x, int y, int width, int height, int offsetX, int offsetY) {
        this.animations = new HashMap<>() {{
            put(AnimationState.IDLE, animation);
        }};
        this.worldX = x;
        this.worldY = y;
        this.animationState = AnimationState.IDLE;
        this.collisionType = CollisionType.SOLID;
        this.collisionBox = new CollisionBox(x, y, offsetX, offsetY, width, height);
        this.layer = Layers.WALL;
        this.collisionMask = 0;
    }

    @Override
    public void update(double delta) {
        animate();
    }

    @Override
    public void onCollision(Body other, EventCollector collector) {
    }
}
