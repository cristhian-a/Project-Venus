package com.next.engine.scene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LevelData(
        int playerSpawnX, int playerSpawnY
) {
}
