package com.next.system;

public final class Settings {

    public static final int TILE_SIZE = 16;    // 16x16

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

        public int ORIGINAL_WIDTH = MAX_SCREEN_COL * TILE_SIZE;
        public int ORIGINAL_HEIGHT = MAX_SCREEN_ROW * TILE_SIZE;

        public int SCALE = 4;
        public int SCALED_TILE_SIZE = TILE_SIZE * SCALE;
        public int WIDTH = SCALED_TILE_SIZE * MAX_SCREEN_COL;
        public int HEIGHT = SCALED_TILE_SIZE * MAX_SCREEN_ROW;
    }
}
