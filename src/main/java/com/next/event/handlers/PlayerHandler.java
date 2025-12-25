package com.next.event.handlers;

import com.next.engine.event.EventDispatcher;
import com.next.event.DialogueEvent;
import com.next.graphics.GameplayUIState;
import com.next.model.NpcDummy;

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
