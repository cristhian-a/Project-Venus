package com.next.engine.system;

public final class Settings {

    public VideoSettings video;

    public static final class VideoSettings {
        private final int DEFAULT_SCREEN_COL = 16;
        private final int DEFAULT_SCREEN_ROW = 12;
        private final int TILE_SIZE = 16;          // TODO: Shall be discarded as the info is held by world rules now

        public int UNSCALED_WIDTH = DEFAULT_SCREEN_COL * TILE_SIZE;
        public int UNSCALED_HEIGHT = DEFAULT_SCREEN_ROW * TILE_SIZE;

        public int SCALE = 4;
        public int SCALED_TILE_SIZE = TILE_SIZE * SCALE;
        public int WIDTH = SCALED_TILE_SIZE * DEFAULT_SCREEN_COL;
        public int HEIGHT = SCALED_TILE_SIZE * DEFAULT_SCREEN_ROW;
    }
}
