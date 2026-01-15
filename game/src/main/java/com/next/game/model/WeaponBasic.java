package com.next.game.model;

import com.next.engine.model.HitboxSpec;
import com.next.engine.scene.Direction;

public class WeaponBasic implements Weapon {

    private final int might;
    private final int iconId;
    private final String name;
    private final String description;

    public WeaponBasic(int might, int iconId, String name, String description) {
        this.might = might;
        this.iconId = iconId;
        this.name = name;
        this.description = description;
    }

    @Override
    public int getMight() {
        return might;
    }

    @Override
    public HitboxSpec getSpec(Direction direction) {
        return null;
    }

    @Override
    public int getIcon() {
        return iconId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getInfo() {
        return description;
    }
}
