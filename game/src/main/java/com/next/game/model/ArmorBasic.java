package com.next.game.model;

public class ArmorBasic implements Armor {

    private final int iconId;
    private final String name;
    private final int resistance;
    private final String description;

    public ArmorBasic(int iconId, String name, String description, int resistance) {
        this.iconId = iconId;
        this.name = name;
        this.resistance = resistance;
        this.description = description;
    }

    @Override
    public int getResistance() {
        return resistance;
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
