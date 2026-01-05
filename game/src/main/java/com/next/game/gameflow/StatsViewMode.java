package com.next.game.gameflow;

import com.next.game.Game;
import com.next.game.ui.StatsViewUIState;

public class StatsViewMode implements GameMode {

    @Override
    public void onEnter(Game game) {
        game.getUi().setState(new StatsViewUIState(game));
    }

    @Override
    public void onExit(Game game) {

    }

    @Override
    public void update(Game game, double delta) {
        game.getScene().submitRender(game.getMailbox().postRender());
    }
}
