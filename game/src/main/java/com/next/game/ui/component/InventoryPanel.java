package com.next.game.ui.component;

import com.next.engine.data.Registry;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.ui.*;
import com.next.engine.ui.component.ActionComponent;
import com.next.engine.ui.widget.ImageNode;
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
                0f
        );
//        var button1 = new Button("Hit 1", Fonts.DEFAULT, (_, _) -> IO.println("Hit 1"));
//        bodyPanel.add(button1);

        for (int i = 0; i < 8; i++) {
            var img = new ImageNode("apple.png", true);
            final int index = i;
            img.addComponent(new ActionComponent((_, _) -> IO.println("Hit: " + index)));
            bodyPanel.add(img);
        }

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
