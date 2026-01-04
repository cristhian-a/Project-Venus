package com.next.rules;

import com.next.engine.event.TriggerRules;
import com.next.event.FallDamageEvent;
import com.next.event.FinishGameEvent;
import com.next.model.Player;

public final class Actions {

    private Actions() {}

    public static final TriggerRules.Action END_GAME =
            (self, other) -> new FinishGameEvent();

    public static TriggerRules.Action damagePlayer(int damage) {
        return (self, other) -> new FallDamageEvent((Player) other, damage);
    }
}
