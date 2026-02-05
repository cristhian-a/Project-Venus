package com.next.game.ui.component;

import com.next.engine.ui.*;
import com.next.game.Game;
import com.next.game.model.Item;
import com.next.game.util.Colors;
import com.next.game.util.Fonts;

final class ItemInfoPanel extends FramePanel {

    // String building utilities
    private static final String EMPTY = "";
    private static final String LEFT_SQR_BRACKET = "[";
    private static final String RIGHT_SQR_BRACKET = "]";

    private final StringBuilder stringBuilder = new StringBuilder();

    // Elements
    private final Panel root;
    private final Label headerLabel;
    private final TextBlock textBlock;

    ItemInfoPanel(Game game, float x, float y, float width, float height) {
        super(
                new Rect(x, y, width, height),
                new com.next.engine.ui.AbsoluteLayout(),
                8f,
                16,
                4,
                Colors.WHITE,
                Colors.BLACK
        );
        Rect frameBounds = contentBounds();

        Panel header = new Panel(
                new Rect(0, 0, frameBounds.width, 30),
                new AbsoluteLayout(),
                0f
        );
        Panel body = new Panel(
                new Rect(0, 0, frameBounds.width, frameBounds.height - 30),
                new VerticalStackLayout(0f),
                0f
        );

        root = new Panel(new Rect(frameBounds), new VerticalStackLayout(0), 0f);
        root.add(header);
        root.add(body);
        add(root);

        headerLabel = new Label(EMPTY, Fonts.DEFAULT, Colors.WHITE);
        header.add(headerLabel);

        textBlock = new TextBlock(EMPTY, Fonts.DEFAULT, Colors.WHITE);
        body.add(textBlock);
    }

    private ItemWidget prev;

    public void update(Focusable focused) {
        if (focused instanceof ItemWidget widget) {
            if (widget.equals(prev)) return;

            prev = widget;
            Item item = widget.getItem();
            stringBuilder.setLength(0);
            var s = stringBuilder.append(LEFT_SQR_BRACKET).append(item.getName()).append(RIGHT_SQR_BRACKET).toString();
            headerLabel.setText(s);

            var txt = item.getInfo();
            textBlock.setText(txt);
        }
    }
}
