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

    public ViewInventory(Player player) {
        FramePanel frame = FrameFactory.dialog(x, y, w, h);
        UI.ROOT.add(frame);

//        FramePanel root = new FramePanel(new Rect(0, 0, w, h), new VerticalStackLayout(4f),
//                0f,
//                16,
//                4,
//                Colors.RED,
//                Colors.BLUE);
        Panel root = new Panel(new Rect(0, 0, w, h), new VerticalStackLayout(4f), 0f);
        frame.add(root);

        Panel headerPanel = new Panel(new Rect(0, 0, w, 30), new AlignedLayout(Align.START, Align.START), 0f);
        var label = new Label(HEADER, Fonts.DEFAULT, Colors.WHITE, Align.CENTER, Align.START);
        headerPanel.add(label);

//        Panel bodyPanel = new Panel(new Rect(0, 0, w, h - 30), new VerticalStackLayout(4f), 0f);
//        var bodyTxt = new Label("Hello World!", Fonts.DEFAULT, Colors.WHITE, Align.CENTER, Align.START);
//        bodyPanel.add(bodyTxt);

        root.add(headerPanel);
//        root.add(bodyPanel);
    }

    public void render(RenderQueue queue) {
        UI.ROOT.layout();
        UI.ROOT.draw(queue);
    }
}
