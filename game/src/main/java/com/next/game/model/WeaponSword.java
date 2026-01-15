package com.next.game.model;

import com.next.engine.Global;
import com.next.engine.data.Registry;
import com.next.engine.model.HitboxSpec;
import com.next.engine.scene.Direction;
import com.next.game.rules.Layers;

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

    // cached specs
    private HitboxSpec downSpec, upSpec, leftSpec, rightSpec;

    @Override
    public HitboxSpec getSpec(Direction direction) {
        double duration = Global.fixedDelta * 30d;
        float knockback = 50f * (float) Global.fixedDelta;

        switch (direction) {
            case DOWN -> {
                if (downSpec == null) {
                    downSpec = new HitboxSpec(-2, 7, 5, 14,
                            duration, 1, 0, knockback,
                            Layers.ENEMY | Layers.WALL, true, true);
                }
                return downSpec;
            }
            case UP -> {
                if (upSpec == null) {
                    upSpec = new HitboxSpec(-2, -18, 5, 14,
                            duration, 1, 0, -knockback,
                            Layers.ENEMY | Layers.WALL, true, true);
                }
                return upSpec;
            }
            case LEFT -> {
                if (leftSpec == null) {
                    leftSpec = new HitboxSpec(-20, 0, 14, 5,
                            duration, 1, -knockback, 0,
                            Layers.ENEMY | Layers.WALL, true, true);
                }
                return leftSpec;
            }
            case RIGHT -> {
                if (rightSpec == null) {
                    rightSpec = new HitboxSpec(6, 0, 14, 5,
                            duration, 1, knockback, 0,
                            Layers.ENEMY | Layers.WALL, true, true);
                }
                return rightSpec;
            }
        }

        throw new RuntimeException("Invalid direction");
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
