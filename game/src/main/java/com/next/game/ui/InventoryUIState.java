package com.next.game.ui;

import com.next.engine.graphics.RenderQueue;
import com.next.game.Game;
import com.next.game.ui.element.InventoryPanel;

public final class InventoryUIState implements UIState {

    private final InventoryPanel inventoryPanel;

    public InventoryUIState(Game game) {
        inventoryPanel = new InventoryPanel(game);
    }

    @Override
    public void update(double delta) {
        inventoryPanel.update();
    }

    @Override
    public void submitRender(RenderQueue queue) {
        inventoryPanel.render(queue);
    }
}
