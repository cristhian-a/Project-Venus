package com.next.gameflow;

import com.next.Game;
import com.next.ui.StartMenuUIState;

public final class TitleMode implements GameMode {
    @Override
    public void onEnter(Game game) {
        game.getUi().setState(new StartMenuUIState(game.getInput(), game.getDispatcher()));
    }

    @Override
    public void onExit(Game game) {

    }

    @Override
    public void update(Game game, double delta) {

    }
}
