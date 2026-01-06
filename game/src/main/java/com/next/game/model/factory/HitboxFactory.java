package com.next.game.model.factory;

import com.next.engine.data.HitboxPool;
import com.next.engine.event.TriggerRule;
import com.next.engine.model.Actor;
import com.next.engine.model.Hitbox;
import com.next.engine.model.HitboxSpec;
import com.next.engine.scene.Scene;
import lombok.Setter;

public final class HitboxFactory {

    @Setter private Scene scene;

    public HitboxFactory(Scene scene) {
        this.scene = scene;
    }

    public void spawnHitbox(Actor actor, HitboxSpec specs, TriggerRule rule) {
        Hitbox hb = HitboxPool.obtain();
        hb.init(actor, actor.getWorldX(), actor.getWorldY(), specs);
        hb.setRule(rule);
        scene.add(hb);
    }
}
