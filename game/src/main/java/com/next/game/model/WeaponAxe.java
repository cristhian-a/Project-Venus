package com.next.game.model;

import com.next.engine.Global;
import com.next.engine.data.Registry;
import com.next.engine.model.HitboxSpec;
import com.next.engine.scene.Direction;
import com.next.game.rules.Layers;

public class WeaponAxe extends WeaponAbstract implements Weapon {

    private static final String NAME = "Axe";
    private static final String DESCRIPTION = "A sharp axe.";

    // pre loaded specs
    private final HitboxSpec downSpec, upSpec, leftSpec, rightSpec;

    public WeaponAxe() {
        int might = 2;
        int sprite = Registry.textureIds.get("axe.png");
        super(might, sprite, NAME, DESCRIPTION);

        boolean singleHit = true;
        boolean followOwner = true;
        int layer = Layers.ENEMY | Layers.WALL;
        double duration = Global.fixedDelta * 30d;
        float knockback = 50f * (float) Global.fixedDelta;

        downSpec = new HitboxSpec(-2, 7, 5, 10,
                duration, 1, 0, knockback, layer, singleHit, followOwner);
        upSpec = new HitboxSpec(-2, -14, 5, 10,
                duration, 1, 0, -knockback, layer, singleHit, followOwner);
        leftSpec = new HitboxSpec(-16, 0, 10, 5,
                duration, 1, -knockback, 0, layer, singleHit, followOwner);
        rightSpec = new HitboxSpec(6, 0, 10, 5,
                duration, 1, knockback, 0, layer, singleHit, followOwner);
    }

    @Override
    public HitboxSpec getSpec(Direction direction) {
        return switch (direction) {
            case DOWN -> downSpec;
            case UP -> upSpec;
            case LEFT -> leftSpec;
            case RIGHT -> rightSpec;
        };
    }
}
