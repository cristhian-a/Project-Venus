package com.next.game.model;

import com.next.engine.data.Registry;

public class ArmorShieldWood implements Armor {

    private static final String NAME = "Wood Shield";
    private static final String DESCRIPTION = "A wooden shield.";

    private final int iconId;

    public ArmorShieldWood() {
        iconId = Registry.textureIds.get("wood-shield.png");
    }

    @Override
    public int getResistance() {
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
