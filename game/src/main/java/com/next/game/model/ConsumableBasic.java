package com.next.game.model;

import com.next.game.rules.ConsumableAction;

public class ConsumableBasic implements Consumable {

    private final int iconId;
    private final String name;
    private final String description;
    private final ConsumableAction action;

    public ConsumableBasic(int iconId, String name, String description, ConsumableAction action) {
        this.iconId = iconId;
        this.name = name;
        this.description = description;
        this.action = action;
    }

    @Override
    public void use() {
        action.use(this);
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
