package com.next.game.model.factory;

import com.next.engine.animation.*;
import com.next.engine.data.Registry;
import com.next.engine.physics.CollisionBox;
import com.next.game.animation.AnimationState;
import com.next.game.model.Player;
import com.next.engine.scene.LevelData;
import com.next.engine.scene.World;

public class PlayerFactory {

    private final World world;
    private final LevelData level;
    private final HitboxFactory hitboxFactory;

    public PlayerFactory(World world, LevelData level, HitboxFactory hitboxFactory) {
        this.world = world;
        this.level = level;
        this.hitboxFactory = hitboxFactory;
    }

    public Player create() {
        var animBuilder = Animation.builder().loop(true).frameRate(20);

        int idleFront   = Registry.textureIds.get("char-1.png");
        int walkDown1   = Registry.textureIds.get("char-2.png");
        int walkDown2   = Registry.textureIds.get("char-3.png");
        int idleBack    = Registry.textureIds.get("char-4.png");
        int walkUp1     = Registry.textureIds.get("char-5.png");
        int walkUp2     = Registry.textureIds.get("char-6.png");
        int idleRight   = Registry.textureIds.get("char-7.png");
        int walkRight1  = Registry.textureIds.get("char-8.png");
        int walkRight2  = Registry.textureIds.get("char-9.png");
        int idleLeft    = Registry.textureIds.get("char-10.png");
        int walkLeft1   = Registry.textureIds.get("char-11.png");
        int walkLeft2   = Registry.textureIds.get("char-12.png");
        int atkFront1   = Registry.textureIds.get("char-atk-1.png");
        int atkFront2   = Registry.textureIds.get("char-atk-2.png");
        int atkBack1    = Registry.textureIds.get("char-atk-3.png");
        int atkBack2    = Registry.textureIds.get("char-atk-4.png");
        int atkRight1   = Registry.textureIds.get("char-atk-5.png");
        int atkRight2   = Registry.textureIds.get("char-atk-6.png");
        int atkLeft1    = Registry.textureIds.get("char-atk-7.png");
        int atkLeft2    = Registry.textureIds.get("char-atk-8.png");

        animBuilder.frames(new int[] { walkDown1, walkDown2 });
        var downAnimation = animBuilder.build();

        animBuilder.frames(new int[] { walkUp1, walkUp2 });
        var upAnimation = animBuilder.build();

        animBuilder.frames(new int[] { walkRight1, walkRight2 });
        var rightAnimation = animBuilder.build();

        animBuilder.frames(new int[] { walkLeft1, walkLeft2 });
        var leftAnimation = animBuilder.build();

        animBuilder.frameRate(10);  // adjusted framerate for attacks
        animBuilder.frames(new int[] { atkFront1, atkFront2, atkFront2, atkFront2 });
        var atkFrontAnimation = animBuilder.build();

        animBuilder.frames(new int[] { atkBack1, atkBack2, atkBack2, atkBack2 });
        var atkBackAnimation = animBuilder.build();

        animBuilder.frames(new int[] { atkRight1, atkRight2, atkRight2, atkRight2 });
        var atkRightAnimation = animBuilder.build();

        animBuilder.frames(new int[] { atkLeft1, atkLeft2, atkLeft2, atkLeft2 });
        var atkLeftAnimation = animBuilder.build();

        Costume idleUpCostume = new StaticCostume(idleBack);
        Costume idleDownCostume = new StaticCostume(idleFront);
        Costume idleLeftCostume = new StaticCostume(idleLeft);
        Costume idleRightCostume = new StaticCostume(idleRight);
        Costume walkDownCostume = new AnimatedCostume(downAnimation);
        Costume walkUpCostume = new AnimatedCostume(upAnimation);
        Costume walkRightCostume = new AnimatedCostume(rightAnimation);
        Costume walkLeftCostume = new AnimatedCostume(leftAnimation);
        Costume atkFrontCostume = new AnimatedCostume(atkFrontAnimation);
        Costume atkBackCostume = new AnimatedCostume(atkBackAnimation);
        Costume atkRightCostume = new AnimatedCostume(atkRightAnimation);
        Costume atkLeftCostume = new AnimatedCostume(atkLeftAnimation);

        Wardrobe<AnimationState> wardrobe = new EnumWardrobe<>(AnimationState.class);
        wardrobe.add(AnimationState.IDLE_DOWN, idleDownCostume);
        wardrobe.add(AnimationState.IDLE_UP, idleUpCostume);
        wardrobe.add(AnimationState.IDLE_LEFT, idleLeftCostume);
        wardrobe.add(AnimationState.IDLE_RIGHT, idleRightCostume);
        wardrobe.add(AnimationState.WALK_DOWN, walkDownCostume);
        wardrobe.add(AnimationState.WALK_UP, walkUpCostume);
        wardrobe.add(AnimationState.WALK_RIGHT, walkRightCostume);
        wardrobe.add(AnimationState.WALK_LEFT, walkLeftCostume);
        wardrobe.add(AnimationState.ATTACK_UP, atkBackCostume);
        wardrobe.add(AnimationState.ATTACK_DOWN, atkFrontCostume);
        wardrobe.add(AnimationState.ATTACK_LEFT, atkLeftCostume);
        wardrobe.add(AnimationState.ATTACK_RIGHT, atkRightCostume);

        int spawnX = world.getTileSize() * level.playerSpawnX() + 8;
        int spawnY = world.getTileSize() * level.playerSpawnY() + 8;

        float offsetX = -5;
        float offsetY = -2;
        float width = 11;
        float height = 9;
        CollisionBox box = new CollisionBox(spawnX, spawnY, offsetX, offsetY, width, height);

        return new Player(spawnX, spawnY, wardrobe, box, hitboxFactory);
    }

}
