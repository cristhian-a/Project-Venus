package com.next.ui;

import com.next.Game;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.model.Player;
import com.next.util.Colors;
import com.next.util.Fonts;

public class StatsViewUIState implements UIState {

    // Box rectangles and stroke info
    private static final int arc = 65;
    private static final int thickness = 5;
    private static final int x = 50, y = 100;
    private static final int w = 400, h = 400;
    private static final int bx = x + (thickness >> 1), by = y + (thickness >> 1);
    private static final int bw = w - thickness + 1, bh = h - thickness + 1;
    private static final int bArc = Math.max(0, arc - thickness);

    // Static text coordinates
    private static final int stX = bx + 125, stY = by + 40;

    private static final int tx = bx + 20;
    private static final int ty = by + 80;
    private static final int incrY = 35;

    private static final int t1Y = ty;
    private static final int t2Y = ty + (incrY);
    private static final int t3Y = ty + (incrY * 2);
    private static final int t4Y = ty + (incrY * 3);
    private static final int t5Y = ty + (incrY * 4);
    private static final int t6Y = ty + (incrY * 5);
    private static final int t7Y = ty + (incrY * 6);
    private static final int t8Y = ty + (incrY * 7);
    private static final int t9Y = ty + (incrY * 8);

    private static final String STATS = "- YOUR STATS -";
    private static final String HP = "Health: ";
    private static final String GEAR = "Gear: ";
    private static final String STRENGTH = "Strength: ";
    private static final String RESISTANCE = "Resistance: ";
    private static final String ATTACK = "Attack: ";
    private static final String DEFENSE = "Defense: ";
    private static final String LEVEL = "Level: ";
    private static final String XP = "XP: ";
    private static final String NEXT_LEVEL = "Next Level: ";
    private static final String COIN = "Coins: ";

    private final String hp;
    private final String strength;
    private final String resistance;
    private final String attack;
    private final String defense;
    private final String level;
    private final String xp;
    private final String nextLevel;
    private final String coins;

    private final Game game;
    private final Player player;

    public StatsViewUIState(Game game) {
        this.game = game;
        this.player = game.getPlayer();

        var attributes = player.getAttributes();
        var gear = player.getActiveGear();

        hp = HP + player.getHealth() + "/" + player.getMaxHealth();
        strength = STRENGTH + attributes.strength;
        resistance = RESISTANCE + attributes.resistance;
        attack = ATTACK + player.getAttack();
        defense = DEFENSE + player.getDefense();
        level = LEVEL + attributes.level;
        xp = XP + attributes.xp;
        nextLevel = NEXT_LEVEL + (attributes.level * 100);
        coins = COIN + attributes.coin;
    }

    @Override
    public void update(double delta) {
    }

    @Override
    public void submitRender(RenderQueue queue) {
        queue.roundStrokeRect(
                Layer.UI_SCREEN,
                x, y,
                w, h,
                thickness,
                Colors.WHITE,
                arc
        );
        queue.fillRoundRect(
                Layer.UI_SCREEN,
                bx, by,
                bw, bh,
                Colors.FADED_BLACK,
                bArc
        );

        Layer l = Layer.UI_SCREEN;
        String f = Fonts.DEFAULT;
        int c = Colors.WHITE;
        RenderPosition rp = RenderPosition.AXIS;
        int fr = 0;

        queue.submit(l, STATS,      f, c, stX, stY, rp, fr);
        queue.submit(l, hp,         f, c, tx, t1Y, rp, fr);
        queue.submit(l, level,      f, c, tx, t2Y, rp, fr);
        queue.submit(l, strength,   f, c, tx, t3Y, rp, fr);
        queue.submit(l, resistance, f, c, tx, t4Y, rp, fr);
        queue.submit(l, attack,     f, c, tx, t5Y, rp, fr);
        queue.submit(l, defense,    f, c, tx, t6Y, rp, fr);
        queue.submit(l, xp,         f, c, tx, t7Y, rp, fr);
        queue.submit(l, nextLevel,  f, c, tx, t8Y, rp, fr);
        queue.submit(l, coins,      f, c, tx, t9Y, rp, fr);
    }
}
