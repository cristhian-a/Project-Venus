package com.next.event.handlers;

import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.event.AttackEvent;

public class CombatHandler {

    private final Mailbox mailbox;
    private final EventDispatcher dispatcher;

    public CombatHandler(Mailbox mailbox, EventDispatcher dispatcher) {
        this.mailbox = mailbox;
        this.dispatcher = dispatcher;

        dispatcher.register(AttackEvent.class, this::onAttack);
    }

    public void onAttack(AttackEvent event) {
        event.target().takeDamage(event.damage());
    }
}
