package com.next.game.rules;

import com.next.game.model.Consumable;

@FunctionalInterface
public interface ConsumableAction {
    void use(Consumable consumable);
}
