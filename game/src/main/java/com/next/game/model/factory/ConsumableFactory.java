package com.next.game.model.factory;

import com.next.engine.data.Registry;
import com.next.game.model.ConsumableAction;
import com.next.game.model.ConsumableBasic;
import com.next.game.model.Player;

public class ConsumableFactory {
    private final Player player;

    public ConsumableFactory(Player player) {
        this.player = player;
    }

    public ConsumableBasic createApple() {
        int texture = Registry.textureIds.get("apple.png");
        String name = "Apple";
        String description = "Biting it will make you feel better.";
        ConsumableAction action = (consumable) -> {
            int hp = player.getHealth();
            hp += 2;
            hp = Math.clamp(hp, 0, player.getMaxHealth());
            player.setHealth(hp);
        };
        return new ConsumableBasic(texture, name, description, action);
    }
}
