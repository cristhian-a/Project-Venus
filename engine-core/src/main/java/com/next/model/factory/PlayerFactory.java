package com.next.model.factory;

import com.next.engine.animation.Animation;
import com.next.engine.data.Registry;
import com.next.model.Player;
import com.next.world.LevelData;
import com.next.world.World;

public class PlayerFactory {

    private final World world;
    private final LevelData level;

    public PlayerFactory(World world, LevelData level) {
        this.world = world;
        this.level = level;
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

        animBuilder.frames(new int[] { walkDown1, walkDown2 });
        var downAnimation = animBuilder.build();

        animBuilder.frames(new int[] { walkUp1, walkUp2 });
        var upAnimation = animBuilder.build();

        animBuilder.frames(new int[] { walkRight1, walkRight2 });
        var rightAnimation = animBuilder.build();

        animBuilder.frames(new int[] { walkLeft1, walkLeft2 });
        var leftAnimation = animBuilder.build();

        int spawnX = world.getTileSize() * level.playerSpawnX();
        int spawnY = world.getTileSize() * level.playerSpawnY();

        return new Player(idleFront, spawnX, spawnY, upAnimation, downAnimation, leftAnimation, rightAnimation);
    }

}
