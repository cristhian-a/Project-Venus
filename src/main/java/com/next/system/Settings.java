package com.next.system;

public final class Settings {

    public static final int ORIGINAL_TILE_SIZE = 16;    // 16x16

    public ControlSettings controls;
    public VideoSettings video;

    public static final class ControlSettings {
        public int up;
        public int down;
        public int left;
        public int right;
        public int debugMode1;
    }

    public static final class VideoSettings {
        public final int MAX_SCREEN_COL = 16;
        public final int MAX_SCREEN_ROW = 12;
        public final int ORIGINAL_TILE_SIZE = Settings.ORIGINAL_TILE_SIZE;

        public int ORIGINAL_WIDTH = MAX_SCREEN_COL * ORIGINAL_TILE_SIZE;
        public int ORIGINAL_HEIGHT = MAX_SCREEN_ROW * ORIGINAL_TILE_SIZE;

        public int SCALE = 4;
        public int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
        public int WIDTH = TILE_SIZE * MAX_SCREEN_COL;
        public int HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;
    }
}
