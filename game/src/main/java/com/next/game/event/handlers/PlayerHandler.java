package com.next.game.event.handlers;

import com.next.engine.event.EventDispatcher;
import com.next.game.event.DialogueEvent;
import com.next.game.ui.GameplayUIState;
import com.next.game.model.NpcDummy;

public class PlayerHandler {

    private final EventDispatcher dispatcher;
    private final GameplayUIState gameplayUIState;

    public PlayerHandler(EventDispatcher dispatcher, GameplayUIState gameplayUIState) {
        this.dispatcher = dispatcher;
        this.gameplayUIState = gameplayUIState;

        dispatcher.register(DialogueEvent.class, this::onFire);
    }

    public void onFire(DialogueEvent event) {
        boolean isDialogue = gameplayUIState.toggleDialogue();

        if (event.other() instanceof NpcDummy dummy) {
//            event.player().setTalking(event.player().isTalking());
            dummy.setBehave(!isDialogue);
        }
    }
}
