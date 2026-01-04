package com.next.ui;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public final class UIWorldModel {

    @AllArgsConstructor
    public static final class HealthBar {
        public int entityId;
        public double ttl;
    }

    @Getter private final List<HealthBar> healthBars = new ArrayList<>();

    public void clear() {
        healthBars.clear();
    }
}
