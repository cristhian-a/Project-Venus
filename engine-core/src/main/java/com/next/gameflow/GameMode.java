package com.next.gameflow;

import com.next.Game;

public interface GameMode {
    void onEnter(Game game);
    void onExit(Game game);
    void update(Game game, double delta);
}
