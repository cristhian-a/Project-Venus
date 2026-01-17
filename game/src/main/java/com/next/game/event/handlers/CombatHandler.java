package com.next.game.event.handlers;

import com.next.engine.Global;
import com.next.engine.data.Mailbox;
import com.next.engine.event.EventDispatcher;
import com.next.game.event.*;
import com.next.game.model.Combatant;
import com.next.game.model.DestructibleTile;
import com.next.game.model.Player;
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

        dispatcher.register(StrikeEvent.class, this::onStrike);
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

    public void onStrike(StrikeEvent event) {
        if (event.target() instanceof Combatant c) {
            dispatcher.dispatch(new AttackEvent(event.striker(), c, event.spec()));
        } else if (event.target() instanceof DestructibleTile d) {
            dispatcher.dispatch(new TreeHitEvent(event.striker(), d, event.spec()));
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
        att.lupXP += (att.level * 100);

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
