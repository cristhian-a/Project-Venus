package com.next.game.model;

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
