package com.next.game.model;

import com.next.engine.model.Entity;
import com.next.engine.model.HitboxSpec;
import com.next.game.event.AttackEvent;
import com.next.game.event.handlers.InteractionContext;
import com.next.game.rules.data.Attributes;

public interface Combatant extends Damageable, Hittable {
    int getId();

    Attributes getAttributes();
    int getMaxHealth();
    int getHealth();
    void setHealth(int health);
    int getAttack();
    int getDefense();

    boolean isDead();

    @Override
    default void onHit(Entity striker, HitboxSpec spec, InteractionContext ctx) {
        if (striker instanceof Combatant c) {
            ctx.dispatch(new AttackEvent(c, this, spec));
        }
    }
}
