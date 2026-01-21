package com.next.game.model;

public class MaterialBasic implements Item {

    private final int spriteId;
    private final String name;
    private final String description;

    public MaterialBasic(int spriteId, String name, String description) {
        this.spriteId = spriteId;
        this.name = name;
        this.description = description;
    }

    @Override
    public int getIcon() {
        return spriteId;
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
