package com.next.io;

import com.next.system.Settings;

import java.io.IOException;

public class Loader {

    public static class SettingsLoader {

        public static Settings load() {
            var file = FileReader.getFile("/settings.json");
            try {
                return JsonReader.readObject(file, Settings.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
