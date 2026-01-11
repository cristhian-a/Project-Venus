package com.next.game.model;

import com.next.engine.data.Registry;

public class WeaponSword implements Weapon {

    private static final String NAME = "Grandpa's Sword";
    private static final String DESCRIPTION = "The old grandpa's sword that \nsaw action during his time in \nwar.";

    private final int iconId;

    public WeaponSword() {
        iconId = Registry.textureIds.get("sword-1.png");
    }

    @Override
    public int getMight() {
        return 1;
    }

    @Override
    public int getIcon() {
        return iconId;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getInfo() {
        return DESCRIPTION;
    }
}
