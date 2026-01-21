package com.next.game.model;

import com.next.engine.model.HitboxSpec;
import com.next.engine.scene.Direction;

public abstract class WeaponAbstract implements Weapon {

    protected final int might;
    protected final int iconId;
    protected final String name;
    protected final String description;

    public WeaponAbstract(int might, int iconId, String name, String description) {
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
    public abstract HitboxSpec getSpec(Direction direction);

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
