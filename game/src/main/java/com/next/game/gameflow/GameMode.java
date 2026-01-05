package com.next.game.gameflow;

import com.next.game.Game;

public interface GameMode {
    void onEnter(Game game);
    void onExit(Game game);
    void update(Game game, double delta);
}
