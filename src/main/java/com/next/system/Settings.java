package com.next.system;

public final class Settings {

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
        public final int ORIGINAL_TILE_SIZE = 16;  // 16x16

        public int scale = 4;
        public int TILE_SIZE = ORIGINAL_TILE_SIZE * scale;
        public int width = TILE_SIZE * MAX_SCREEN_COL;
        public int height = TILE_SIZE * MAX_SCREEN_ROW;
    }
}
