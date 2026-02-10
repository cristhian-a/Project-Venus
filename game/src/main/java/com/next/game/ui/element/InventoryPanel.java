package com.next.game.ui.element;

import com.next.engine.graphics.RenderQueue;
import com.next.engine.ui.*;
import com.next.engine.ui.component.Action;
import com.next.engine.ui.style.StyleEngine;
import com.next.engine.ui.style.StyleSheet;
import com.next.engine.ui.widget.Button;
import com.next.game.Game;
import com.next.game.ui.InputSolver;
import com.next.game.util.Colors;
import com.next.game.util.Fonts;

import java.util.Map;

public final class InventoryPanel {

    // Inventory bounds
    private static final int x = 570, y = 100;
    private static final int w = 400, h = 400;

    // Info panel bounds
    private static final int infoX = 570, infoY = 508;
    private static final int infoW = 400, infoH = 150;

    // Static data
    private static final String HEADER = "- YOUR STUFF -";

    // Inventory mode
    private enum Mode { GRID, ACTIONS }
    private Mode mode = Mode.GRID;

    // Element's panels
    private final UIRoot uiroot;
    private final InputSolver inputSolver;
    private final InventoryGridPanel grid;
    private final ItemInfoPanel infoPanel;
    private final StyleSheet styleSheet = new StyleSheet();

    public InventoryPanel(Game game) {
        // Main grid
        grid = new InventoryGridPanel(game, new Rect(x, y, w, h), inventoryAction());
        // Information panel
        infoPanel = new ItemInfoPanel(game, infoX, infoY, infoW, infoH);
        infoPanel.onBack(this::hideActions);
        infoPanel.onDrop(this::removeFocusedSlot);

        final float WIDTH = 1024, HEIGHT = 768;
        uiroot = new UIRoot(new Rect(0, 0, WIDTH, HEIGHT));
        uiroot.add(grid);
        uiroot.add(infoPanel);

        inputSolver = new InputSolver(game.getInput(), uiroot);

        styleSheet.addRule(".Button", Map.of(
                "backgroundColor", 0x88FF0000,
                "textColor", 0xFFFFFF00
        ));
        styleSheet.addRule(".Button:focused", Map.of(
                "backgroundColor", 0xFF0000FF
        ));
        StyleEngine styleEngine = new StyleEngine(styleSheet);
        uiroot.setStyleEngine(styleEngine);

        AbstractContainer testContainer =
                new AbstractContainer(new Rect(100, 100, 400, 550), new VerticalStackLayout(0f));
        uiroot.add(testContainer);

        var cont1 = new AbstractContainer(new Rect(0, 0, 386, 200), new VerticalStackLayout(0f));
        testContainer.add(cont1);
        var b1 = new Button("Test1", Fonts.DEFAULT, (_, _) -> { IO.println("Clicked!"); });
        var b2 = new Button("Test2", Fonts.DEFAULT, (_, _) -> { IO.println("Clicked!"); });
        var b3 = new Button("Test3", Fonts.DEFAULT, (_, _) -> { IO.println("Clicked!"); });
        cont1.add(b1);
        cont1.add(b2);
        cont1.add(b3);
        var cont2 = new AbstractContainer(new Rect(0, 0, 386, 100));
        testContainer.add(cont2);
        var cont3 = new AbstractContainer(new Rect(0, 0, 386, 100), new HorizontalStackLayout(0f));
        testContainer.add(cont3);
        var btn = new Button("Test1", Fonts.DEFAULT, (_, _) -> { IO.println("Clicked!"); });
        var btn2 = new Button("Test2", Fonts.DEFAULT, (_, _) -> { IO.println("Clicked!"); });
        var btn3 = new Button("Test3", Fonts.DEFAULT, (_, _) -> { IO.println("Clicked!"); });
        cont3.add(btn);
        cont3.add(btn2);
        cont3.add(btn3);

        styleSheet.addRule(".AbstractContainer", Map.of(
                "padding", 8f
        ));
        styleSheet.addRule(".Button", Map.of(
                "marginLeft", 25f,
                "marginBottom", 10f
        ));
    }

    private Focusable focusedSlot;

    public void update() {
        inputSolver.update();
        if (mode == Mode.GRID) {
            focusedSlot = inputSolver.getFocused();
        }
        infoPanel.update(focusedSlot);
    }

    public void render(RenderQueue queue) {
        uiroot.render(queue);
    }

    private Action inventoryAction() {
        return (AbstractNode node, String _) -> {
            if (!(node instanceof ItemSlot slot)) return;

            mode = Mode.ACTIONS;
            infoPanel.setActionsFor(slot.getItem());

            uiroot.getFocusManager().pushScope(infoPanel);
            uiroot.getFocusManager().requestFocus(infoPanel.getFirstAction());
            uiroot.markDirty();
        };
    }

    private void hideActions() {
        mode = Mode.GRID;
        uiroot.markDirty();
        uiroot.getFocusManager().requestFocus(focusedSlot);
        uiroot.getFocusManager().popScope();
    }

    private void removeFocusedSlot() {
        if (focusedSlot instanceof ItemSlot i) {
            grid.popSlot(i);

            var fm = uiroot.getFocusManager();
            fm.rebuild();
            fm.focusNext();
            focusedSlot = fm.getFocused();
        }
    }

}
