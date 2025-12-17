package com.next.io;

import com.next.world.LevelData;
import com.next.world.WorldRules;

import java.io.IOException;

public final class Loader {

    public static class Settings {
        public static com.next.system.Settings load() {
            var file = FileReader.getFile("/settings.json");
            try {
                if (file == null) throw new IOException("Settings file not found");
                return JsonReader.readObject(file, com.next.system.Settings.class);
            } catch (IOException e) { throw new RuntimeException(e); }
        }
    }

    public static class World {
        public static WorldRules load(String fileName) throws IOException {
            var file = FileReader.getFile("/configuration/" + fileName);
            return JsonReader.readObject(file, WorldRules.class);
        }
    }

    public static class Level {
        public static LevelData load(String fileName) throws IOException {
            var file = FileReader.getFile("/configuration/" + fileName);
            return JsonReader.readObject(file, LevelData.class);
        }
    }

}
