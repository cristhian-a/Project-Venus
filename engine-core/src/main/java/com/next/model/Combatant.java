package com.next.model;

import com.next.rules.data.Attributes;

public interface Combatant {
    int getId();

    Attributes getAttributes();
    int getMaxHealth();
    int getHealth();
    void setHealth(int health);
    int getAttack();
    int getDefense();

    void takeDamage(int damage);
}
