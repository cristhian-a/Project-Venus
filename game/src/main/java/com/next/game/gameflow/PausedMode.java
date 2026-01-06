package com.next.game.gameflow;

import com.next.game.Game;
import com.next.engine.sound.PauseSound;
import com.next.game.util.Sounds;
import com.next.game.ui.PausedUIState;

public final class PausedMode implements GameMode {

    @Override
    public void onEnter(Game game) {
        game.getUi().setState(new PausedUIState());
        game.getDispatcher().dispatch(new PauseSound(Sounds.WIND));
    }

    @Override
    public void onExit(Game game) {
    }

    @Override
    public void update(Game game, double delta) {
        game.getScene().submitRender(game.getMailbox().postRender());
    }
}
