package com.next.game.rules;

import com.next.engine.event.TriggerRules;
import com.next.game.model.Player;

public final class Conditions {

    private Conditions() {}

    public static final TriggerRules.Condition IS_PLAYER =
            (self, other) -> other instanceof Player;

}
