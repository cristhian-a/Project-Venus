package com.next.game.model;

import com.next.engine.model.Entity;
import com.next.engine.model.HitboxSpec;
import com.next.game.event.handlers.InteractionContext;

public interface Hittable {
    void onHit(Entity striker, HitboxSpec spec, InteractionContext ctx);
}
