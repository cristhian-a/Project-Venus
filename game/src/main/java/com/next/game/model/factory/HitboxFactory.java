package com.next.game.model.factory;

import com.next.engine.data.HitboxPool;
import com.next.engine.data.ProjectilePool;
import com.next.engine.event.TriggerRule;
import com.next.engine.event.TriggerRules;
import com.next.engine.model.*;
import com.next.engine.scene.Scene;
import com.next.game.event.AttackEvent;
import com.next.game.model.Combatant;
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

    public void projectile(float worldX, float worldY, Combatant striker, ProjectileSpec spec, int spriteId) {
        Projectile p = ProjectilePool.obtain();

        var rule = TriggerRules
                .when((s, other) -> other instanceof Combatant c && c != striker && !c.isDead())
                .then((s, other) -> {
                    if (!spec.penetrable()) p.dispose();
                    return new AttackEvent(striker, (Combatant) other, spec.hitboxSpec());
                });

        Hitbox hb = HitboxPool.obtain();
        hb.setRule(rule);
        scene.add(hb);

        p.init(spriteId, worldX, worldY, hb, spec);
        scene.add(p);
    }
}
