package com.next.game.ui.component;

import com.next.engine.graphics.RenderQueue;
import com.next.engine.ui.*;
import com.next.engine.ui.component.Action;
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
    private final UIRoot uiroot;
    private final InputSolver inputSolver;

    // Info panel
    private final ItemInfoPanel infoPanel;

    public InventoryPanel(Game game) {
        FramePanel frame = FrameFactory.dialog(x, y, w, h);

        // Inventory panel
        Panel rootPanel = new Panel(new Rect(frame.contentBounds()), new VerticalStackLayout(0f), 0f);
        frame.add(rootPanel);

        Panel headerPanel = new Panel(
                new Rect(0, 0, rootPanel.getContentWidth(), 30),
                new AbsoluteLayout(),
                0f
        );
        var label = new Label(HEADER, Fonts.DEFAULT, Colors.WHITE, Align.CENTER, Align.CENTER);
        headerPanel.add(label);

        Panel bodyPanel = new Panel(
                new Rect(0, 0, rootPanel.getContentWidth(), rootPanel.getContentHeight() - 60),
                new GridLayout(4, 42f, 32f),
                0f
        );

//        for (int i = 0; i < 16; i++) {
//            var img = new ImageNode("apple.png", true);
//            final int index = i;
//            img.addComponent(new ActionComponent((_, _) -> IO.println("Hit: " + index)));
//            bodyPanel.add(img);
//        }
        var inventory = game.getPlayer().getInventory();
        inventory.forEach((item) -> {
            var itemWidget = new ItemWidget(item);
            itemWidget.addComponent(new ActionComponent(this.inventoryAction()));
            bodyPanel.add(itemWidget);
        });

        rootPanel.add(headerPanel);  // remember to add the panels to the root panel
        rootPanel.add(bodyPanel);

        final float WIDTH = 1024, HEIGHT = 768;
        uiroot = new UIRoot(new Rect(0, 0, WIDTH, HEIGHT));
        uiroot.add(frame);
        //frame.setAnchor(Align.CENTER, Align.CENTER);

        // Information panel
        infoPanel = new ItemInfoPanel(game, infoX, infoY, infoW, infoH);
        uiroot.add(infoPanel);

        inputSolver = new InputSolver(game.getInput(), uiroot);
    }

    public void update() {
        inputSolver.update();
        infoPanel.update(inputSolver.getFocused());
    }

    public void render(RenderQueue queue) {
        uiroot.render(queue);
    }

    private Action inventoryAction() {
        return (AbstractNode node, String input) -> {
            IO.println(node);
        };
    }
}
