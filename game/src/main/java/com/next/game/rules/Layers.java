package com.next.game.rules;

public final class Layers {
    private Layers() {}

    public static final int PLAYER =        1 << 0;
    public static final int NPC =           1 << 1;
    public static final int ITEM =          1 << 2;
    public static final int WALL =          1 << 3;
    public static final int SENSOR =        1 << 4;
    public static final int ENEMY =         1 << 5;
    public static final int PROJECTILE =    1 << 6;
}
