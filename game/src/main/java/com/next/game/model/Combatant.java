package com.next.game.model;

import com.next.game.rules.data.Attributes;

public interface Combatant {
    int getId();

    Attributes getAttributes();
    int getMaxHealth();
    int getHealth();
    void setHealth(int health);
    int getAttack();
    int getDefense();

    void takeDamage(int damage);
    boolean isDead();
}
