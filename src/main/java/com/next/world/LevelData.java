package com.next.world;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LevelData(
        int playerSpawnX, int playerSpawnY
) {
}
