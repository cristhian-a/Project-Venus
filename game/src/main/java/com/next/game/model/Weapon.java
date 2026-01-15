package com.next.game.model;

import com.next.engine.model.HitboxSpec;
import com.next.engine.scene.Direction;

public interface Weapon extends Equip {
    int getMight();
    HitboxSpec getSpec(Direction direction);
}
