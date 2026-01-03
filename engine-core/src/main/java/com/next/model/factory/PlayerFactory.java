package com.next.model.factory;

import com.next.engine.animation.Animation;
import com.next.engine.animation.AnimationState;
import com.next.engine.data.Registry;
import com.next.engine.physics.CollisionBox;
import com.next.model.Player;
import com.next.world.LevelData;
import com.next.world.World;

import java.util.HashMap;
import java.util.Map;

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

        var idleDown = new Animation(new int[]{idleFront}, 0, false);
        var idleUp = new Animation(new int[]{idleBack}, 0, false);
        var idleLft = new Animation(new int[]{idleLeft}, 0, false);
        var idleRgt = new Animation(new int[]{idleRight}, 0, false);

        Map<AnimationState, Animation> animations = new HashMap<>();
        animations.put(AnimationState.IDLE_DOWN, idleDown);
        animations.put(AnimationState.IDLE_UP, idleUp);
        animations.put(AnimationState.IDLE_LEFT, idleLft);
        animations.put(AnimationState.IDLE_RIGHT, idleRgt);
        animations.put(AnimationState.WALK_DOWN, downAnimation);
        animations.put(AnimationState.WALK_UP, upAnimation);
        animations.put(AnimationState.WALK_RIGHT, rightAnimation);
        animations.put(AnimationState.WALK_LEFT, leftAnimation);
        animations.put(AnimationState.ATTACK_UP, atkBackAnimation);
        animations.put(AnimationState.ATTACK_DOWN, atkFrontAnimation);
        animations.put(AnimationState.ATTACK_LEFT, atkLeftAnimation);
        animations.put(AnimationState.ATTACK_RIGHT, atkRightAnimation);

        int spawnX = world.getTileSize() * level.playerSpawnX() + 8;
        int spawnY = world.getTileSize() * level.playerSpawnY() + 8;

        float offsetX = -5;
        float offsetY = -2;
        float width = 11;
        float height = 9;
        CollisionBox box = new CollisionBox(spawnX, spawnY, offsetX, offsetY, width, height);

        return new Player(spawnX, spawnY, animations, box, hitboxFactory);
    }

}
