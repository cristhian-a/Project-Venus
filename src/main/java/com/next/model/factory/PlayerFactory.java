package com.next.model.factory;

import com.next.core.Animation;
import com.next.model.Player;

public class PlayerFactory {

    public static Player createPlayer() {
        var animBuilder = Animation.builder().loop(true).frameRate(20);

        animBuilder.frames(new int[] { 3, 4 });
        var downAnimation = animBuilder.build();

        animBuilder.frames(new int[] { 6, 7 });
        var upAnimation = animBuilder.build();

        animBuilder.frames(new int[] { 9, 10 });
        var rightAnimation = animBuilder.build();

        animBuilder.frames(new int[] { 12, 13 });
        var leftAnimation = animBuilder.build();

        return new Player(2, upAnimation, downAnimation, leftAnimation, rightAnimation);
    }
}
