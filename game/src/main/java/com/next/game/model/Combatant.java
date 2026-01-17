package com.next.game.model;

import com.next.game.rules.data.Attributes;

public interface Combatant extends Damageable {
    int getId();

    Attributes getAttributes();
    int getMaxHealth();
    int getHealth();
    void setHealth(int health);
    int getAttack();
    int getDefense();

    boolean isDead();
}
