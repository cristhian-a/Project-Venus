package com.next.event.handlers;

import com.next.engine.Global;
import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.event.AttackEvent;
import com.next.event.LevelUpEvent;
import com.next.event.UiDamageEvent;
import com.next.model.Combatant;
import com.next.model.Player;
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
        for (int i = knockbacks.size() - 1; i >= 0; i--) {
            var k = knockbacks.get(i);
            mailbox.motionQueue.submit(k.target.getId(), k.knockbackX, k.knockbackY, 0);
            k.remainingTime -= delta;

            if (k.remainingTime <= 0) {
                knockbacks.remove(k);
            }
        }
    }

    public void onAttack(AttackEvent event) {
        int atkV = event.striker().getAttack();
        int defV = event.target().getDefense();

        int damage = atkV - defV;
        damage = Math.max(1, damage);   // 1 damage is the minimum
        event.target().takeDamage(damage);

        dispatcher.dispatch(new UiDamageEvent(event.target().getId()));

        if (event.target().getHealth() > 0) {
            double remaining = Global.fixedDelta * 10;
            knockbacks.add(new Knockback(event.target(), event.spec().knockbackX(), event.spec().knockbackY(), remaining));
        }

        if (event.target().isDead() && event.striker() instanceof Player player) {
            var att = player.getAttributes();
            att.xp += 50;
            if (att.xp >= att.lupXP) {
                levelUp(player);
            }
        }
    }

    private void levelUp(Player player) {
        var att = player.getAttributes();
        att.level++;
        att.xp -= att.lupXP;
        att.strength++;
        att.resistance++;

        dispatcher.dispatch(new LevelUpEvent(player));
    }

    @AllArgsConstructor
    private static class Knockback {
        Combatant target;
        float knockbackX;
        float knockbackY;
        double remainingTime;
    }
}
