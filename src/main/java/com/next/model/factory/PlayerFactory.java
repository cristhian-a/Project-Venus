package com.next.model.factory;

import com.next.engine.animation.Animation;
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

        animBuilder.frames(new int[] { 3, 4 });
        var downAnimation = animBuilder.build();

        animBuilder.frames(new int[] { 6, 7 });
        var upAnimation = animBuilder.build();

        animBuilder.frames(new int[] { 9, 10 });
        var rightAnimation = animBuilder.build();

        animBuilder.frames(new int[] { 12, 13 });
        var leftAnimation = animBuilder.build();

        int spawnX = world.getTileSize() * level.playerSpawnX();
        int spawnY = world.getTileSize() * level.playerSpawnY();

        return new Player(2, spawnX, spawnY, upAnimation, downAnimation, leftAnimation, rightAnimation);
    }

}
