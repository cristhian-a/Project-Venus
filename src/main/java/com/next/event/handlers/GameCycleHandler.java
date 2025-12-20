package com.next.event.handlers;

import com.next.engine.Global;
import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.event.FinishGameEvent;

public class GameCycleHandler {

    private final Mailbox mailbox;
    private final EventDispatcher dispatcher;

    public GameCycleHandler(EventDispatcher dispatcher, Mailbox mailbox) {
        this.mailbox = mailbox;
        this.dispatcher = dispatcher;

        dispatcher.register(FinishGameEvent.class, this::onFire);
    }

    public void onFire(FinishGameEvent event) {
        IO.println("GAME OVER");
        Global.end();
    }
}
