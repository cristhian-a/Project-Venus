package com.next.game.ui.component;

import com.next.engine.graphics.RenderQueue;
import com.next.engine.ui.*;
import com.next.game.Game;
import com.next.game.ui.InputSolver;
import com.next.game.util.Colors;
import com.next.game.util.Fonts;

public final class InventoryPanel {

    // Inventory bounds
    private static final int x = 570, y = 100;
    private static final int w = 400, h = 400;

    // Info panel bounds
    private static final int infoX = 570, infoY = 508;
    private static final int infoW = 400, infoH = 150;

    // Static data
    private static final String HEADER = "- YOUR STUFF -";

    // Root panel
    private final UIRoot root;
    private final InputSolver inputSolver;

    public InventoryPanel(Game game) {
        FramePanel frame = FrameFactory.dialog(x, y, w, h);

        // Inventory panel
        Panel root = new Panel(new Rect(frame.contentBounds()), new VerticalStackLayout(0f), 0f);
        frame.add(root);

        Panel headerPanel = new Panel(
                new Rect(0, 0, root.getContentWidth(), 30),
                new AbsoluteLayout(),
                0f
        );
        var label = new Label(HEADER, Fonts.DEFAULT, Colors.WHITE, Align.CENTER, Align.CENTER);
        headerPanel.add(label);

        Panel bodyPanel = new Panel(
                new Rect(0, 0, root.getContentWidth(), root.getContentHeight() - 60),
                new GridLayout(4, 8f, 8f),
                2f
        );
        var button1 = new Button("Hit 1", Fonts.DEFAULT, (_, _) -> IO.println("Hit 1"));
            button1.anchorX(Align.CENTER);
        var button2 = new Button("Hit 2", Fonts.DEFAULT, (_, _) -> IO.println("Hit 2"));
            button2.anchorX(Align.CENTER);
        var button3 = new Button("Hit 3", Fonts.DEFAULT, (_, _) -> IO.println("Hit 3"));
            button3.anchorX(Align.CENTER);
        var button4 = new Button("Hit 4", Fonts.DEFAULT, (_, _) -> IO.println("Hit 4"));
            button4.anchorX(Align.CENTER);
        var button5 = new Button("Hit 5", Fonts.DEFAULT, (_, _) -> IO.println("Hit 5"));
            button5.anchorX(Align.CENTER);
        var button6 = new Button("Hit 6", Fonts.DEFAULT, (_, _) -> IO.println("Hit 6"));
            button6.anchorX(Align.CENTER);
        bodyPanel.add(button1);
        bodyPanel.add(button2);
        bodyPanel.add(button3);
        bodyPanel.add(button4);
        bodyPanel.add(button5);
        bodyPanel.add(button6);

        root.add(headerPanel);  // remember to add the panels to the root panel
        root.add(bodyPanel);

        // Information panel
        var infoPanel = infoPanel();

        final float WIDTH = 1024, HEIGHT = 768;
        this.root = new UIRoot(new Rect(0, 0, WIDTH, HEIGHT));
        this.root.add(frame);
//        frame.setAnchor(Align.CENTER, Align.CENTER);
        this.root.add(infoPanel);

        inputSolver = new InputSolver(game.getInput(), this.root);
    }

    private AbstractNode infoPanel() {
        FramePanel frame = FrameFactory.dialog(infoX, infoY, infoW, infoH);
        Rect rootRect = frame.contentBounds();

        Panel root = new Panel(new Rect(rootRect), new VerticalStackLayout(0), 0f);
        frame.add(root);

        Panel header = new Panel(
                new Rect(0, 0, rootRect.width, 30),
                new AbsoluteLayout(),
                0f
        );
//        header.add(nameLabel);

        Panel body = new Panel(
                new Rect(0, 0, rootRect.width, rootRect.height - 30),
                new VerticalStackLayout(0f),
                0f
        );
//        body.add(textBlock);

        root.add(header);
        root.add(body);
        return frame;
    }

    public void update() {
        inputSolver.update();
    }

    public void render(RenderQueue queue) {
        root.render(queue);
    }
}
