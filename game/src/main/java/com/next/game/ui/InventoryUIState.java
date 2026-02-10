package com.next.game.ui;

import com.next.engine.graphics.RenderQueue;
import com.next.game.Game;
import com.next.game.ui.element.CharacterStatsView;
import com.next.game.ui.element.InventoryPanel;

public final class InventoryUIState implements UIState {

//    private final InventoryPanel inventoryPanel;
    private final CharacterStatsView characterStatsView;

    public InventoryUIState(Game game) {
//        inventoryPanel = new InventoryPanel(game);
        characterStatsView = new CharacterStatsView(game);
    }

    @Override
    public void update(double delta) {
//        inventoryPanel.update();
        characterStatsView.update();
    }

    @Override
    public void submitRender(RenderQueue queue) {
//        inventoryPanel.render(queue);
        characterStatsView.render(queue);
    }
}
