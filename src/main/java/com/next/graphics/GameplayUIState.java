package com.next.graphics;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderQueue;
import com.next.model.Player;

public class GameplayUIState implements UIState {

    private Player player;

    public GameplayUIState(Player player) {
        this.player = player;
    }

    @Override
    public void update(double delta) {
    }

    @Override
    public void submitRender(RenderQueue queue) {
        for (int i = 0; i < player.getHeldKeys().size(); i++) {
            var key = player.getHeldKeys().get(i);
            queue.submit(Layer.UI, 20 + 75 * i, 5, key.getSpriteId());
        }
    }
}
