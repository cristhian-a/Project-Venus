package com.next.game.ui.element;

import com.next.engine.graphics.RenderQueue;
import com.next.engine.ui.*;
import com.next.engine.ui.component.Action;
import com.next.engine.ui.style.StyleEngine;
import com.next.engine.ui.style.StyleSheet;
import com.next.game.Game;
import com.next.game.ui.InputSolver;

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
