package com.next.io;

import com.next.engine.dto.TextureMetadata;
import com.next.engine.io.FileReader;
import com.next.engine.io.JsonReader;
import com.next.engine.sound.SoundClip;
import com.next.engine.sound.SoundData;
import com.next.util.Sounds;
import com.next.world.LevelData;
import com.next.world.WorldRules;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

    public static class Controls {
        public static Map<String, Integer> loadActionMap() {
            var file = FileReader.getFile("/controls.json");
            try {
                return JsonReader.readMap(file, String.class, Integer.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Fonts {
        public static InputStream load() {
            return FileReader.getFile("/fonts/x12y16pxMaruMonica.ttf");
        }
    }

    public static class Worlds {
        public static WorldRules load(String fileName) throws IOException {
            var file = FileReader.getFile("/configuration/" + fileName);
            return JsonReader.readObject(file, WorldRules.class);
        }
    }

    public static class Levels {
        public static LevelData load(String fileName) throws IOException {
            var file = FileReader.getFile("/configuration/" + fileName);
            return JsonReader.readObject(file, LevelData.class);
        }
    }

    public static class Audio {

        /**
         * Load using javax.sound.AudioInputStream
         * @return {@link Map} of {@link SoundData} indexed by {@link String} track name
         * @throws RuntimeException if an error occurs while loading the audio
         */
        public static Map<SoundClip, SoundData> load() {
            var tracks = new HashMap<SoundClip, String>();
            tracks.put(Sounds.FANFARE, "/sounds/end_sound.wav");
            tracks.put(Sounds.WIND, "/sounds/wind.wav");
            tracks.put(Sounds.PICK_UP, "/sounds/pick_up.wav");
            tracks.put(Sounds.SPELL_UP, "/sounds/spell_up.wav");
            tracks.put(Sounds.OPEN_DOOR, "/sounds/door.wav");

            Map<SoundClip, SoundData> data = new HashMap<>();
            for (var track : tracks.keySet()) {
                URL u = FileReader.getResource(tracks.get(track));
                try {
                    AudioInputStream stream = AudioSystem.getAudioInputStream(u);
                    SoundData sd = new SoundData();
                    sd.format = stream.getFormat();
                    sd.pcmData = stream.readAllBytes();
                    data.put(track, sd);
                } catch (UnsupportedAudioFileException | IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return data;
        }
    }

    public static final class Textures {

        public static BufferedImage loadImage(String fileName) throws IOException {
            return ImageIO.read(FileReader.getFile("/textures/" + fileName));
        }

        public static TextureMetadata loadMetadata(String fileName) throws IOException {
            return JsonReader.readObject(FileReader.getFile("/textures/" + fileName), TextureMetadata.class);
        }
    }

}
