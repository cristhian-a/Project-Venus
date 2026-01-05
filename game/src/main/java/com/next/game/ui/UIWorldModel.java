package com.next.game.ui;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public final class UIWorldModel {

    public static final class HealthBar {
        public int entityId;
        public double ttl;
    }

    @Getter private final Map<Integer, HealthBar> healthBars = new HashMap<>();

    public void clear() {
        healthBars.clear();
    }
}
