package com.next.game.ui;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.game.model.Item;
import com.next.game.util.Colors;
import com.next.game.util.Fonts;

public class ViewItemInfo {

    // Box rectangles and stroke info
    private static final int arc = 25;
    private static final int thickness = 5;
    private static final int x = 570, y = 508;
    private static final int w = 400, h = 150;
    private static final int bx = x + (thickness >> 1), by = y + (thickness >> 1);
    private static final int bw = w - thickness + 1, bh = h - thickness + 1;
    private static final int bArc = Math.max(0, arc - thickness);

    // Static text coordinates
    private static final int nameX = bx + 15, nameY = by + 40;
    private static final int tX = nameX, tY = nameY + 30;
    private static final int infX = tX + 15, infY = tY + 25;

    private static final String EMPTY_TXT = "";
    private static final String LINE_SEP = "\n";
    private static final String LEFT_SQR_BRACKET = "[";
    private static final String RIGHT_SQR_BRACKET = "]";

    private String infoText;
    private String[] displayText;
    private String displayName = EMPTY_TXT;

    private boolean itemSelected;
    private String[] options;
    private int optionIndex;

    public void update(Item item, boolean itemSelected, String[] options, int optionIndex) {
        this.itemSelected = itemSelected;
        this.optionIndex = optionIndex;
        this.options = options;

        String inf = EMPTY_TXT;
        if (item != null) inf = item.getInfo();

        if (inf.equals(infoText)) return;
        infoText = inf;
        buildDisplayText();

        if (item != null) displayName = LEFT_SQR_BRACKET + item.getName() + RIGHT_SQR_BRACKET;
        else displayName = EMPTY_TXT;
    }

    private void buildDisplayText() {
        displayText = infoText.split(LINE_SEP);
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

        queue.submit(l, displayName, f, c, nameX, nameY, rp, fr);

        if (itemSelected) {
            queue.submit(l, ">", f, c, tX + (optionIndex * 100), infY, rp, fr); // cursor

            for (int i = 0; i < options.length; i++) {
                queue.submit(l, options[i], f, c, infX + (i * 100), infY, rp, fr);
            }
        } else {
            for (int i = 0; i < displayText.length; i++) {
                queue.submit(l, displayText[i], f, c, tX, tY + (25 * i), rp, fr);
            }
        }
    }
}
