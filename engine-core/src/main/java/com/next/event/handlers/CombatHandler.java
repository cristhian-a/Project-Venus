package com.next.event.handlers;

import com.next.engine.Global;
import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.event.AttackEvent;
import com.next.model.Combatant;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class CombatHandler {

    private final Mailbox mailbox;
    private final EventDispatcher dispatcher;

    private final List<Knockback> knockbacks = new ArrayList<>();

    public CombatHandler(Mailbox mailbox, EventDispatcher dispatcher) {
        this.mailbox = mailbox;
        this.dispatcher = dispatcher;

        dispatcher.register(AttackEvent.class, this::onAttack);
    }

    public void update(double delta) {
        for (int i = 0; i < knockbacks.size(); i++) {
            var k = knockbacks.get(i);
            mailbox.motionQueue.submit(k.target.getId(), k.knockbackX, k.knockbackY, 0);
            k.remainingTime -= delta;

            if (k.remainingTime <= 0) {
                knockbacks.remove(k);
            }
        }
    }

    public void onAttack(AttackEvent event) {
        event.target().takeDamage(event.spec().damage());

        if (event.target().getHealth() > 0) {
            double remaining = Global.fixedDelta * 10;
            knockbacks.add(new Knockback(event.target(), event.spec().knockbackX(), event.spec().knockbackY(), remaining));
        }
    }

    @AllArgsConstructor
    private static class Knockback {
        Combatant target;
        float knockbackX;
        float knockbackY;
        double remainingTime;
    }
}
