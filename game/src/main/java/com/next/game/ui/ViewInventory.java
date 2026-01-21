package com.next.game.ui;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.game.model.Player;
import com.next.game.util.Colors;
import com.next.game.util.Fonts;

public final class ViewInventory {

    // Box rectangles and stroke info
    private static final int arc = 25;
    private static final int thickness = 5;
    private static final int x = 570, y = 100;
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

    private static final String HEADER = "- YOUR STUFF -";

    public ViewInventory(Player player) {

    }

    public void render(RenderQueue queue) {
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

        queue.submit(l, HEADER,      f, c, stX, stY, rp, fr);
    }
}
