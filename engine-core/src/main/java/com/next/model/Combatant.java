package com.next.model;

public interface Combatant {
    int getMaxHealth();
    int getHealth();
    void setHealth(int health);
    int getId();
    void takeDamage(int damage);
}
