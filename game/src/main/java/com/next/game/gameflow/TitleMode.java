package com.next.game.gameflow;

import com.next.game.Game;
import com.next.game.ui.StartMenuUIState;

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
