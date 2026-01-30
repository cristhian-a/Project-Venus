package com.next.game.ui;

import com.next.engine.graphics.RenderQueue;
import com.next.engine.ui.*;
import com.next.game.model.Player;
import com.next.game.ui.component.*;
import com.next.game.util.Colors;
import com.next.game.util.Fonts;

public final class ViewInventory {

    // Box rectangles and stroke info
    private static final int x = 570, y = 100;
    private static final int w = 400, h = 400;

    private static final String HEADER = "- YOUR STUFF -";

    private final FramePanel frame;

    public ViewInventory(Player player) {
        frame = FrameFactory.dialog(x, y, w, h);

        Panel root = new Panel(new Rect(frame.contentBounds()), new VerticalStackLayout(0f), 0f);
        frame.add(root);

        Panel headerPanel = new Panel(
                new Rect(0, 0, root.getContentWidth(), 30),
                new AlignedLayout(Align.CENTER, Align.START),
                0f
        );
        var label = new Label(HEADER, Fonts.DEFAULT, Colors.WHITE, Align.CENTER, Align.START);
        headerPanel.add(label);

        Panel bodyPanel = new Panel(
                new Rect(0, 0, root.getContentWidth(), root.getContentHeight() - 60),
                new VerticalStackLayout(4f),
                0f
        );
        var bodyTxt = new Label("Hello World!", Fonts.DEFAULT, Colors.WHITE, Align.CENTER, Align.START);
        var bodyTxt2 = new Label("How are you there?", Fonts.DEFAULT, Colors.WHITE, Align.CENTER, Align.START);
        bodyPanel.add(bodyTxt);
        bodyPanel.add(bodyTxt2);

        Panel footerPanel = new Panel(
                new Rect(0, 0, root.getContentWidth(), 30),
                new AbsoluteLayout(),
                0f
        );
        var footerTxt = new Label(30, 0, "Hello World!", Fonts.DEFAULT, Colors.WHITE);
        footerPanel.add(footerTxt);

        root.add(headerPanel);
        root.add(bodyPanel);
        root.add(footerPanel);
    }

    public void render(RenderQueue queue) {
        frame.measure();
        frame.updateLayout();
        frame.draw(queue);
    }
}
